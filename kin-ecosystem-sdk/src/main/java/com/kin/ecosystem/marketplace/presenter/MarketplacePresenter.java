package com.kin.ecosystem.marketplace.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kin.ecosystem.Callback;
import com.kin.ecosystem.base.BasePresenter;
import com.kin.ecosystem.base.Observer;
import com.kin.ecosystem.data.blockchain.BlockchainSource;
import com.kin.ecosystem.data.blockchain.IBlockchainSource;
import com.kin.ecosystem.data.offer.OfferDataSource;
import com.kin.ecosystem.data.order.OrderDataSource;
import com.kin.ecosystem.data.order.OrderRepository;
import com.kin.ecosystem.marketplace.model.NativeSpendOffer;
import com.kin.ecosystem.marketplace.view.IMarketplaceView;
import com.kin.ecosystem.network.model.Offer;
import com.kin.ecosystem.network.model.Offer.ContentTypeEnum;
import com.kin.ecosystem.network.model.Offer.OfferType;
import com.kin.ecosystem.network.model.OfferInfo;
import com.kin.ecosystem.network.model.OfferList;
import com.kin.ecosystem.network.model.Order;
import com.kin.ecosystem.poll.view.PollWebViewActivity.PollBundle;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MarketplacePresenter extends BasePresenter<IMarketplaceView> implements IMarketplacePresenter {

    private static final int NOT_FOUND = -1;

    private final OfferDataSource offerRepository;
    private final OrderDataSource orderRepository;
    private final IBlockchainSource blockchainSource;

    private List<Offer> spendList = new ArrayList<>();
    private List<Offer> earnList = new ArrayList<>();

    private Observer<Offer> pendingOfferObserver;
    private Observer<Order> completedOrderObserver;
    private final Gson gson;

    public MarketplacePresenter(@NonNull final OfferDataSource offerRepository,
        @NonNull final OrderDataSource orderRepository, @Nullable final IBlockchainSource blockchainSource) {
        this.spendList = new ArrayList<>();
        this.earnList = new ArrayList<>();
        this.offerRepository = offerRepository;
        this.orderRepository = orderRepository;
        this.blockchainSource = blockchainSource;
        this.gson = new Gson();
    }

    @Override
    public void onAttach(IMarketplaceView view) {
        super.onAttach(view);
        getCachedOffers();
        listenToPendingOffers();
        listenToCompletedOrders();
    }

    private void getCachedOffers() {
        OfferList cachedOfferList = offerRepository.getCachedOfferList();
        setOfferList(cachedOfferList);
    }

    private void listenToCompletedOrders() {
        completedOrderObserver = new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                getOffers();
            }
        };
        orderRepository.addCompletedOrderObserver(completedOrderObserver);
    }

    private void listenToPendingOffers() {
        pendingOfferObserver = new Observer<Offer>() {
            @Override
            public void onChanged(Offer offer) {
                if (offer != null) {
                    removeOfferFromList(offer);
                }
            }
        };
        offerRepository.getPendingOffer().addObserver(pendingOfferObserver);
    }

    private void removeOfferFromList(Offer offer) {
        int index;
        if (offer.getOfferType() == OfferType.EARN) {
            index = earnList.indexOf(offer);
            if (index != NOT_FOUND) {
                earnList.remove(index);
                notifyEarnItemRemoved(index);
                setEarnEmptyViewIfNeeded();
            }

        } else {
            index = spendList.indexOf(offer);
            if (index != NOT_FOUND) {
                spendList.remove(index);
                notifySpendItemRemoved(index);
                setSpendEmptyViewIfNeeded();
            }
        }
    }

    private void setEarnEmptyViewIfNeeded() {
        if (earnList.size() == 0) {
            if (view != null) {
                view.setEarnEmptyView();
            }
        }
    }

    private void setSpendEmptyViewIfNeeded() {
        if (spendList.size() == 0) {
            if (view != null) {
                view.setSpendEmptyView();
            }
        }
    }

    private void notifyEarnItemRemoved(int index) {
        if (view != null) {
            view.notifyEarnItemRemoved(index);
        }
    }

    private void notifyEarnItemInserted(int index) {
        if (view != null) {
            view.notifyEarnItemInserted(index);
        }
    }

    private void notifySpendItemRemoved(int index) {
        if (view != null) {
            view.notifySpendItemRemoved(index);
        }
    }

    private void notifySpendItemInserted(int index) {
        if (view != null) {
            view.notifySpendItemInserted(index);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        release();
    }

    private void release() {
        offerRepository.getPendingOffer().removeObserver(pendingOfferObserver);
        orderRepository.removeCompletedOrderObserver(completedOrderObserver);
    }

    @Override
    public void getOffers() {
        this.offerRepository.getOffers(new Callback<OfferList>() {
            @Override
            public void onResponse(OfferList offerList) {
                syncOffers(offerList);
            }

            @Override
            public void onFailure(Throwable t) {
                //TODO show error msg
            }
        });
    }

    private void syncOffers(OfferList offerList) {
        if (offerList != null && offerList.getOffers() != null) {
            List<Offer> newEarnOffers = new ArrayList<>();
            List<Offer> newSpendOffers = new ArrayList<>();

            splitOffersByType(offerList.getOffers(), newEarnOffers, newSpendOffers);

            syncList(newEarnOffers, earnList, OfferType.EARN);
            syncList(newSpendOffers, spendList, OfferType.SPEND);

            setEarnEmptyViewIfNeeded();
            setSpendEmptyViewIfNeeded();

        }
    }

    private void syncList(List<Offer> newList, List<Offer> oldList, OfferType offerType) {
        // check if offer should be removed (index changed / removed from list).
        if (newList.size() > 0) {
            for (int i = 0; i < oldList.size(); i++) {
                Offer offer = oldList.get(i);
                int index = newList.indexOf(offer);
                if (index == NOT_FOUND || index != i) {
                    oldList.remove(i);
                    notifyItemRemoved(i, offerType);
                }
            }
        }

        // Add missing offers, the order matters
        for (int i = 0; i < newList.size(); i++) {
            Offer offer = newList.get(i);
            if (i < oldList.size()) {
                if (!oldList.get(i).equals(offer)) {
                    oldList.add(i, offer);
                    notifyItemInserted(i, offerType);
                }
            } else {
                oldList.add(offer);
                notifyItemInserted(i, offerType);
            }
        }
    }

    private void notifyItemRemoved(int index, OfferType offerType) {
        if (isSpend(offerType)) {
            notifySpendItemRemoved(index);
        } else {
            notifyEarnItemRemoved(index);
        }
    }

    private void notifyItemInserted(int index, OfferType offerType) {
        if (isSpend(offerType)) {
            notifySpendItemInserted(index);
        } else {
            notifyEarnItemInserted(index);
        }
    }

    private boolean isSpend(OfferType offerType) {
        return offerType == OfferType.SPEND;
    }

    private void setOfferList(OfferList offerList) {
        if (offerList != null && offerList.getOffers() != null) {
            splitOffersByType(offerList.getOffers(), this.earnList, this.spendList);
        }
        if (this.view != null) {
            this.view.setEarnList(earnList);
            this.view.setSpendList(spendList);
        }
    }

    private void splitOffersByType(List<Offer> list, List<Offer> earnList, List<Offer> spendList) {
        for (Offer offer : list) {
            if (offer.getOfferType() == OfferType.EARN) {
                earnList.add(offer);
            } else {
                spendList.add(offer);
            }
        }
    }

    @Override
    public void onItemClicked(int position, OfferType offerType) {
        final Offer offer;
        if (offerType == OfferType.EARN) {
            offer = earnList.get(position);
            if (this.view != null) {
                PollBundle pollBundle = new PollBundle()
                    .setJsonData(offer.getContent())
                    .setOfferID(offer.getId())
                    .setTitle(offer.getTitle());
                this.view.showOfferActivity(pollBundle);
            }
        } else {
            offer = spendList.get(position);
            if (offer.getContentType() == ContentTypeEnum.EXTERNAL) {
                nativeSpendOfferClicked(offer);
                return;
            }
            int balance = blockchainSource.getBalance();
            final BigDecimal amount = new BigDecimal(offer.getAmount());

            if (balance < amount.intValue()) {
                showToast("You don't have enough Kin");
                return;
            }

            OfferInfo offerInfo = deserializeOfferInfo(offer.getContent());
            if (offerInfo != null) {
                showSpendDialog(offerInfo, offer);
            } else {
                showSomethingWentWrong();
            }
        }
    }

    private void nativeSpendOfferClicked(Offer offer) {
        offerRepository.getNativeSpendOfferObservable().postValue((NativeSpendOffer) offer);
    }

    private void showSomethingWentWrong() {
        if (view != null) {
            view.showSomethingWentWrong();
        }
    }

    @Override
    public void balanceItemClicked() {
        if (view != null) {
            view.navigateToOrderHistory();
        }
    }

    @Override
    public void showOfferActivityFailed() {
        showSomethingWentWrong();
    }

    private void showSpendDialog(@NonNull final OfferInfo offerInfo, @NonNull final Offer offer) {
        if (this.view != null) {
            this.view.showSpendDialog(createSpendDialogPresenter(offerInfo, offer));
        }
    }

    private ISpendDialogPresenter createSpendDialogPresenter(@NonNull final OfferInfo offerInfo,
        @NonNull final Offer offer) {
        return new SpendDialogPresenter(offerInfo, offer, BlockchainSource.getInstance(),
            OrderRepository.getInstance());
    }

    private OfferInfo deserializeOfferInfo(final String content) {
        try {
            return gson.fromJson(content, OfferInfo.class);
        } catch (JsonSyntaxException t) {
            return null;
        }
    }

    private void showToast(String msg) {
        if (view != null) {
            view.showToast(msg);
        }
    }
}

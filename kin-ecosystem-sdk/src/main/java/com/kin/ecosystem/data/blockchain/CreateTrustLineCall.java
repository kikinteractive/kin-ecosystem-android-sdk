package com.kin.ecosystem.data.blockchain;


import kin.sdk.KinAccount;
import kin.sdk.exception.OperationFailedException;

class CreateTrustLineCall extends Thread {

	private final KinAccount account;
	private final TrustlineCallback trustlineCallback;

	private static final int MAX_TRIES = 10;

	CreateTrustLineCall(KinAccount account, TrustlineCallback trustlineCallback) {
		this.account = account;
		this.trustlineCallback = trustlineCallback;

	}

	@Override
	public void run() {
		super.run();
		createTrustline(0);
	}

	private void createTrustline(int tries) {
		try {
			// TODO Activate account not necessary anymore?
//			account.activateSync();
			account.getStatusSync();
			trustlineCallback.onSuccess();
		} catch (OperationFailedException e) {
			if (tries < MAX_TRIES) {
				createTrustline(++tries);
			} else {
				trustlineCallback.onFailure(e);
			}
		}
	}

	interface TrustlineCallback {

		void onSuccess();

		void onFailure(OperationFailedException e);
	}
}

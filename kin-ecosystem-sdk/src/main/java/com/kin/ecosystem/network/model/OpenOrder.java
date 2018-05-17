package com.kin.ecosystem.network.model;/*
 * Kin Ecosystem
 * Apis for client to server interaction
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * an open order that hasn&#x27;t been submitted yet
 */
public class OpenOrder {
    @SerializedName("id")
    private String id = null;
    @SerializedName("offer_id")
    private String offerId = null;

    /**
     * Gets or Sets offerType
     */
    @JsonAdapter(OfferTypeEnum.Adapter.class)
    public enum OfferTypeEnum {

        EARN("earn"),
        SPEND("spend");

        private String value;

        OfferTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static OfferTypeEnum fromValue(String text) {
            for (OfferTypeEnum b : OfferTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        public static class Adapter extends TypeAdapter<OfferTypeEnum> {
            @Override
            public void write(final JsonWriter jsonWriter, final OfferTypeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public OfferTypeEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return OfferTypeEnum.fromValue(String.valueOf(value));
            }
        }
    }

    @SerializedName("offer_type")
    private OfferTypeEnum offerType = null;
    @SerializedName("title")
    private String title = null;
    @SerializedName("description")
    private String description = null;
    @SerializedName("amount")
    private Integer amount = null;
    @SerializedName("blockchain_data")
    private BlockchainData blockchainData = null;
    @SerializedName("expiration_date")
    private String expirationDate = null;

    public OpenOrder id(String id) {
        this.id = id;
        return this;
    }


    /**
     * unique identifier of this item
     * @return id
     **/
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public OpenOrder offerId(String offerId) {
        this.offerId = offerId;
        return this;
    }


    /**
     * the offer which this order was created from
     * @return offerId
     **/
    public String getOfferId() {
        return offerId;
    }
    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public OpenOrder offerType(OfferTypeEnum offerType) {
        this.offerType = offerType;
        return this;
    }


    /**
     * Get offerType
     * @return offerType
     **/
    public OfferTypeEnum getOfferType() {
        return offerType;
    }
    public void setOfferType(OfferTypeEnum offerType) {
        this.offerType = offerType;
    }

    public OpenOrder title(String title) {
        this.title = title;
        return this;
    }


    /**
     * usually a brand name
     * @return title
     **/
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public OpenOrder description(String description) {
        this.description = description;
        return this;
    }


    /**
     * Get description
     * @return description
     **/
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public OpenOrder amount(Integer amount) {
        this.amount = amount;
        return this;
    }


    /**
     * kin amount
     * @return amount
     **/
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public OpenOrder blockchainData(BlockchainData blockchainData) {
        this.blockchainData = blockchainData;
        return this;
    }


    /**
     * Get blockchainData
     * @return blockchainData
     **/
    public BlockchainData getBlockchainData() {
        return blockchainData;
    }
    public void setBlockchainData(BlockchainData blockchainData) {
        this.blockchainData = blockchainData;
    }

    public OpenOrder expirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }


    /**
     * Get expirationDate
     * @return expirationDate
     **/
    public String getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpenOrder openOrder = (OpenOrder) o;
        return this.id.equals(openOrder.id) &&
            this.offerId.equals(openOrder.offerId) &&
            this.offerType.equals(openOrder.offerType) &&
            this.title.equals(openOrder.title) &&
            this.description.equals(openOrder.description) &&
            this.amount.equals(openOrder.amount) &&
            this.blockchainData.equals(openOrder.blockchainData) &&
            this.expirationDate.equals(openOrder.expirationDate);
    }

    @Override
    public int hashCode() {
        return id.hashCode() + offerId.hashCode() + offerType.hashCode() +
            title.hashCode() +  description.hashCode() +  amount.hashCode() +
            blockchainData.hashCode() +  expirationDate.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OpenOrder {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    offerId: ").append(toIndentedString(offerId)).append("\n");
        sb.append("    offerType: ").append(toIndentedString(offerType)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    blockchainData: ").append(toIndentedString(blockchainData)).append("\n");
        sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


}




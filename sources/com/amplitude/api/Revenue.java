package com.amplitude.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Revenue {
    public static final String TAG = "com.amplitude.api.Revenue";
    private static AmplitudeLog logger = AmplitudeLog.getLogger();
    protected Double price = null;
    protected String productId = null;
    protected JSONObject properties = null;
    protected int quantity = 1;
    protected String receipt = null;
    protected String receiptSig = null;
    protected String revenueType = null;

    protected boolean isValidRevenue() {
        if (this.price != null) {
            return true;
        }
        logger.m11w(TAG, "Invalid revenue, need to set price");
        return false;
    }

    public Revenue setProductId(String productId) {
        if (Utils.isEmptyString(productId)) {
            logger.m11w(TAG, "Invalid empty productId");
        } else {
            this.productId = productId;
        }
        return this;
    }

    public Revenue setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Revenue setPrice(double price) {
        this.price = Double.valueOf(price);
        return this;
    }

    public Revenue setRevenueType(String revenueType) {
        this.revenueType = revenueType;
        return this;
    }

    public Revenue setReceipt(String receipt, String receiptSignature) {
        this.receipt = receipt;
        this.receiptSig = receiptSignature;
        return this;
    }

    public Revenue setRevenueProperties(JSONObject revenueProperties) {
        logger.m11w(TAG, "setRevenueProperties is deprecated, please use setEventProperties instead");
        return setEventProperties(revenueProperties);
    }

    public Revenue setEventProperties(JSONObject eventProperties) {
        this.properties = Utils.cloneJSONObject(eventProperties);
        return this;
    }

    protected JSONObject toJSONObject() {
        JSONObject obj = this.properties == null ? new JSONObject() : this.properties;
        try {
            obj.put(Constants.AMP_REVENUE_PRODUCT_ID, this.productId);
            obj.put(Constants.AMP_REVENUE_QUANTITY, this.quantity);
            obj.put(Constants.AMP_REVENUE_PRICE, this.price);
            obj.put(Constants.AMP_REVENUE_REVENUE_TYPE, this.revenueType);
            obj.put(Constants.AMP_REVENUE_RECEIPT, this.receipt);
            obj.put(Constants.AMP_REVENUE_RECEIPT_SIG, this.receiptSig);
        } catch (JSONException e) {
            logger.m5e(TAG, String.format("Failed to convert revenue object to JSON: %s", new Object[]{e.toString()}));
        }
        return obj;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r6) {
        /*
        r5 = this;
        r1 = 1;
        r2 = 0;
        if (r5 != r6) goto L_0x0006;
    L_0x0004:
        r2 = r1;
    L_0x0005:
        return r2;
    L_0x0006:
        if (r6 == 0) goto L_0x0005;
    L_0x0008:
        r3 = r5.getClass();
        r4 = r6.getClass();
        if (r3 != r4) goto L_0x0005;
    L_0x0012:
        r0 = r6;
        r0 = (com.amplitude.api.Revenue) r0;
        r3 = r5.quantity;
        r4 = r0.quantity;
        if (r3 != r4) goto L_0x0005;
    L_0x001b:
        r3 = r5.productId;
        if (r3 == 0) goto L_0x0072;
    L_0x001f:
        r3 = r5.productId;
        r4 = r0.productId;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0029:
        r3 = r5.price;
        if (r3 == 0) goto L_0x0077;
    L_0x002d:
        r3 = r5.price;
        r4 = r0.price;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0037:
        r3 = r5.revenueType;
        if (r3 == 0) goto L_0x007c;
    L_0x003b:
        r3 = r5.revenueType;
        r4 = r0.revenueType;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0045:
        r3 = r5.receipt;
        if (r3 == 0) goto L_0x0081;
    L_0x0049:
        r3 = r5.receipt;
        r4 = r0.receipt;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0053:
        r3 = r5.receiptSig;
        if (r3 == 0) goto L_0x0086;
    L_0x0057:
        r3 = r5.receiptSig;
        r4 = r0.receiptSig;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0005;
    L_0x0061:
        r3 = r5.properties;
        if (r3 == 0) goto L_0x008c;
    L_0x0065:
        r3 = r5.properties;
        r4 = r0.properties;
        r3 = com.amplitude.api.Utils.compareJSONObjects(r3, r4);
        if (r3 != 0) goto L_0x0070;
    L_0x006f:
        r1 = r2;
    L_0x0070:
        r2 = r1;
        goto L_0x0005;
    L_0x0072:
        r3 = r0.productId;
        if (r3 == 0) goto L_0x0029;
    L_0x0076:
        goto L_0x0005;
    L_0x0077:
        r3 = r0.price;
        if (r3 == 0) goto L_0x0037;
    L_0x007b:
        goto L_0x0005;
    L_0x007c:
        r3 = r0.revenueType;
        if (r3 == 0) goto L_0x0045;
    L_0x0080:
        goto L_0x0005;
    L_0x0081:
        r3 = r0.receipt;
        if (r3 == 0) goto L_0x0053;
    L_0x0085:
        goto L_0x0005;
    L_0x0086:
        r3 = r0.receiptSig;
        if (r3 == 0) goto L_0x0061;
    L_0x008a:
        goto L_0x0005;
    L_0x008c:
        r3 = r0.properties;
        if (r3 != 0) goto L_0x006f;
    L_0x0090:
        goto L_0x0070;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amplitude.api.Revenue.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.productId != null) {
            result = this.productId.hashCode();
        } else {
            result = 0;
        }
        int i2 = ((result * 31) + this.quantity) * 31;
        if (this.price != null) {
            hashCode = this.price.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.revenueType != null) {
            hashCode = this.revenueType.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.receipt != null) {
            hashCode = this.receipt.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.receiptSig != null) {
            hashCode = this.receiptSig.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.properties != null) {
            i = this.properties.hashCode();
        }
        return hashCode + i;
    }
}

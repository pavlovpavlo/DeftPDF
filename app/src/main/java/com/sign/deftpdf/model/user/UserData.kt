package com.sign.deftpdf.model.user

import com.google.gson.annotations.SerializedName


class UserData {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("role")
    var role: String? = null

    @SerializedName("api_token")
    var apiToken: String? = null

    @SerializedName("google_id")
    var googleId: String? = null

    @SerializedName("stripe_customer_id")
    var stripeCustomerId: String? = null

    @SerializedName("stripe_subscription_id")
    var stripeSubscriptionId: String? = null

    @SerializedName("stripe_price_id")
    var stripePriceId: String? = null

    @SerializedName("register_token")
    var registerToken: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("avatar")
    var avatar: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("last_activity")
    var lastActivity: String? = null

    @SerializedName("last_confirmation")
    var lastConfirmation: String? = null

    @SerializedName("sign")
    var sign: UserSign? = null

    @SerializedName("initials")
    var initials: UserSign? = null

}
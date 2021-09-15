package com.sign.deftpdf.model.user

import com.squareup.moshi.Json


class UserData {
    @field:Json(name = "id")
    var id: Int? = null

    @field:Json(name = "email")
    var email: String? = null

    @field:Json(name = "role")
    var role: String? = null

    @field:Json(name = "api_token")
    var apiToken: String? = null

    @field:Json(name = "google_id")
    var googleId: String? = null

    @field:Json(name = "stripe_customer_id")
    var stripeCustomerId: String? = null

    @field:Json(name = "stripe_subscription_id")
    var stripeSubscriptionId: String? = null

    @field:Json(name = "stripe_price_id")
    var stripePriceId: String? = null

    @field:Json(name = "register_token")
    var registerToken: String? = null

    @field:Json(name = "status")
    var status: String? = null

    @field:Json(name = "avatar")
    var avatar: String? = null

    @field:Json(name = "name")
    var name: String? = null

    @field:Json(name = "created_at")
    var createdAt: String? = null

    @field:Json(name = "updated_at")
    var updatedAt: String? = null

    @field:Json(name = "last_activity")
    var lastActivity: String? = null

    @field:Json(name = "last_confirmation")
    var lastConfirmation: String? = null

}
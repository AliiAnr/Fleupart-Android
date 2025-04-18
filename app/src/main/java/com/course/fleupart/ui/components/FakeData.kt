package com.course.fleupart.ui.components

import com.course.fleupart.R
import kotlinx.datetime.LocalDate

data class Flower(
    val id: Long,
    val image: Int,
    val storeName: String,
    val flowerName: String,
    val rating: Double,
    val reviewsCount: Int,
    val price: Long
)

data class TipsItem(
    val title: String,
    val description: String,
    val imageRes: Int
)

data class OrderItem(
    val id: Long,
    val imageRes: Int,
    val name: String,
    val price: Long
)

data class MerchantProduct(
    val categories: List<MerchantCategory>
)

data class MerchantCategory(
    val title: String,
    val items: List<OrderItem>
)

data class RecentSales(
    val id: Long,
    val name: String,
    val price: Long,
    val paymentMethod: String
)

data class WithdrawData (
    val date: LocalDate,
    val amount: Long,
    val paymentMethod: String
)

data class OrderItemStatus(
    val id: Long,
    val imageRes: Int,
    val name: String,
    val price: Long,
    val status: String
)

data class BankItem(
    val id: Long,
    val name: String,
    val bankName: String,
    val accountNumber: String,
    val accountName: String
)

object FakeCategory {
    val flowers= listOf(
        Flower(1, R.drawable.flower_1, "Buga Adik", "Rose", 4.5, 100, 10000),
        Flower(2, R.drawable.flower_2, "Flower WwW", "Lily", 4.0, 50, 20000),
        Flower(3, R.drawable.flower_3, "Flwey", "Sunflower", 4.2, 70, 15000),
    )

    val BankList = listOf(
        BankItem(1, "Bunga Adik", "Bank BCA", "1234567890", "Bunga Adik"),
        BankItem(2, "Flower WwW", "Bank BNI", "1234567890", "Flower WwW"),
        BankItem(3, "Flwey", "Bank Mandiri", "1234567890", "Flwey"),
    )

    val recentSalesList = listOf(
        RecentSales(1, "Flower Chocolate", 10000, "Cash"),
        RecentSales(1, "Flower Chocolate", 10000, "Cash"),
        RecentSales(1, "Flower Chocolate", 10000, "Cash"),
        RecentSales(1, "Flower Chocolate", 10000, "Cash"),
    )

    val withdrawData = listOf(
        WithdrawData(LocalDate(2024, 6, 29), 100000, "Debit"),
        WithdrawData(LocalDate(2024, 6, 29), 100000, "Debit"),
        WithdrawData(LocalDate(2024, 6, 29), 100000, "Debit"),
//        WithdrawData(LocalDate(2024, 6, 29), 100000, "Debit"),
//        WithdrawData(LocalDate(2024, 6, 29), 100000, "Debit"),
//        WithdrawData(LocalDate(2024, 6, 29), 100000, "Debit"),
//        WithdrawData(LocalDate(2024, 6, 29), 100000, "Debit"),
    )

    val tipsList = listOf(
        TipsItem(
            title = "How to do a promotion properly",
            description = "Learn how to manage your promotion to increase sale.",
            imageRes = R.drawable.tips_1
        ),
        TipsItem(
            title = "How to present your product",
            description = "Learn how to present your product to increase customer engagement.",
            imageRes = R.drawable.tips_2
        ),
        TipsItem(
            title = "How to increase sale",
            description = "Learn the important steps to maintain a good sale.",
            imageRes = R.drawable.tips_3
        )
    )

    val editProduct = listOf(
        OrderItem(1, R.drawable.cc_1, "Flower Chocolate", 10000),
        OrderItem(2, R.drawable.cc_1, "Flower Bouquet", 20000),
        OrderItem(3, R.drawable.cc_1, "Chocolate Bouquet", 15000),
        OrderItem(4, R.drawable.cc_1, "Flower Bouquet", 20000),
        OrderItem(5, R.drawable.cc_1, "Chocolate Bouquet", 15000),
    )

    val productStatus = listOf(
        OrderItemStatus(1, R.drawable.cc_1, "Flower Chocolate", 10000, "Pending"),
        OrderItemStatus(2, R.drawable.cc_1, "Flower Bouquet", 20000, "On Process"),
        OrderItemStatus(3, R.drawable.cc_1, "Chocolate Bouquet", 15000, "Completed"),
        OrderItemStatus(4, R.drawable.cc_1, "Flower Bouquet", 20000, "Pending"),
        OrderItemStatus(5, R.drawable.cc_1, "Chocolate Bouquet", 15000, "Completed"),
    )

    val merchantCategory = listOf(
        MerchantCategory(
            title = "Flower",
            items = listOf(
                OrderItem(1, R.drawable.cc_1, "Flower Chocolate", 10000),
                OrderItem(2, R.drawable.cc_1, "Flower Bouquet", 20000),
                OrderItem(3, R.drawable.cc_1, "Chocolate Bouquet", 15000),
                OrderItem(4, R.drawable.cc_1, "Flower Bouquet", 20000),
                OrderItem(5, R.drawable.cc_1, "Chocolate Bouquet", 15000),
            )
        ),
        MerchantCategory(
            title = "Chocolate",
            items = listOf(
                OrderItem(1, R.drawable.cc_1, "Flower Chocolate", 10000),
                OrderItem(2, R.drawable.cc_1, "Flower Bouquet", 20000),
                OrderItem(3, R.drawable.cc_1, "Chocolate Bouquet", 15000),
                OrderItem(4, R.drawable.cc_1, "Flower Bouquet", 20000),
                OrderItem(5, R.drawable.cc_1, "Chocolate Bouquet", 15000),
            )
        ),
        MerchantCategory(
            title = "Bouquet",
            items = listOf(
                OrderItem(1, R.drawable.cc_1, "Flower Chocolate", 10000),
                OrderItem(2, R.drawable.cc_1, "Flower Bouquet", 20000),
                OrderItem(3, R.drawable.cc_1, "Chocolate Bouquet", 15000),
                OrderItem(4, R.drawable.cc_1, "Flower Bouquet", 20000),
                OrderItem(5, R.drawable.cc_1, "Chocolate Bouquet", 15000),
            ),
        )
    )
}
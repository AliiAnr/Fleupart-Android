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

data class AccountList(
    val name: String,
    val icon: Int,
)

data class TipsItem(
    val id: Long,
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

data class TipsDetail(
    val id: Long,
    val title: String,
    val description: String,
    val imageRes: Int,
    val content: List<TipsContent>,
    val author: String,
    val readTime: String,
    val publishedDate: String
)

data class TipsContent(
    val subtitle: String,
    val body: String
)

object FakeCategory {

    val tipsDetailList = listOf(
        TipsDetail(
            id = 1,
            title = "How to do a promotion properly",
            description = "Learn how to manage your promotion to increase sale.",
            imageRes = R.drawable.tips_1,
            author = "Marketing Team",
            readTime = "5 min read",
            publishedDate = "January 15, 2025",
            content = listOf(
                TipsContent(
                    subtitle = "Understanding Your Target Audience",
                    body = "Before starting any promotion, identify who your customers are. Understanding demographics, preferences, and buying behavior helps you craft messages that resonate with them."
                ),
                TipsContent(
                    subtitle = "Set Clear Goals",
                    body = "Define what you want to achieve: increase sales, boost brand awareness, or clear inventory. Clear goals help measure success and guide your promotional strategy."
                ),
                TipsContent(
                    subtitle = "Choose the Right Channels",
                    body = "Select platforms where your audience is active. Social media, email marketing, and in-store promotions each have unique advantages. Mix channels for better reach."
                ),
                TipsContent(
                    subtitle = "Create Urgency",
                    body = "Limited-time offers and exclusive deals encourage immediate action. Use countdown timers, limited stock notices, or flash sales to drive quick decisions."
                ),
                TipsContent(
                    subtitle = "Track and Analyze Results",
                    body = "Monitor key metrics like conversion rates, revenue generated, and customer engagement. Use insights to refine future promotions and maximize ROI."
                )
            )
        ),
        TipsDetail(
            id = 2,
            title = "How to present your product",
            description = "Learn how to present your product to increase customer engagement.",
            imageRes = R.drawable.tips_2,
            author = "Product Team",
            readTime = "4 min read",
            publishedDate = "January 10, 2025",
            content = listOf(
                TipsContent(
                    subtitle = "High-Quality Product Photos",
                    body = "Use clear, well-lit images from multiple angles. Show products in use and include close-ups of details. Professional photography builds trust and increases conversion."
                ),
                TipsContent(
                    subtitle = "Write Compelling Descriptions",
                    body = "Focus on benefits, not just features. Tell a story about how the product solves problems or enhances the customer's life. Use emotional and sensory language."
                ),
                TipsContent(
                    subtitle = "Organize Your Product Catalog",
                    body = "Categorize products logically and use filters to help customers find what they need. A clean, organized layout improves user experience and boosts sales."
                ),
                TipsContent(
                    subtitle = "Highlight Best Sellers",
                    body = "Feature top-performing products prominently. Social proof like ratings and reviews builds credibility and influences purchasing decisions."
                ),
                TipsContent(
                    subtitle = "Use Video Demonstrations",
                    body = "Short videos showing product usage, unboxing, or behind-the-scenes content increase engagement and help customers visualize the product better."
                )
            )
        ),
        TipsDetail(
            id = 3,
            title = "How to increase sale",
            description = "Learn the important steps to maintain a good sale.",
            imageRes = R.drawable.tips_3,
            author = "Sales Team",
            readTime = "6 min read",
            publishedDate = "January 5, 2025",
            content = listOf(
                TipsContent(
                    subtitle = "Excellent Customer Service",
                    body = "Respond promptly to inquiries, resolve issues quickly, and go the extra mile. Happy customers become repeat buyers and recommend your store to others."
                ),
                TipsContent(
                    subtitle = "Offer Competitive Pricing",
                    body = "Research competitor pricing and offer value. Consider bundle deals, discounts, or loyalty programs to attract and retain customers."
                ),
                TipsContent(
                    subtitle = "Optimize Product Listings",
                    body = "Use relevant keywords, detailed descriptions, and accurate specifications. Well-optimized listings appear in more searches and convert better."
                ),
                TipsContent(
                    subtitle = "Leverage Customer Reviews",
                    body = "Encourage satisfied customers to leave reviews. Display testimonials prominently. Positive reviews build trust and influence buying decisions."
                ),
                TipsContent(
                    subtitle = "Consistent Marketing Efforts",
                    body = "Maintain regular social media presence, send email newsletters, and run periodic promotions. Consistency keeps your brand top-of-mind for customers."
                ),
                TipsContent(
                    subtitle = "Analyze Sales Data",
                    body = "Track which products sell best, peak sales times, and customer behavior patterns. Use data insights to stock inventory and plan promotions effectively."
                )
            )
        )
    )

    val flowers= listOf(
        Flower(1, R.drawable.flower_1, "Buga Adik", "Rose", 4.5, 100, 10000),
        Flower(2, R.drawable.flower_2, "Flower WwW", "Lily", 4.0, 50, 20000),
        Flower(3, R.drawable.flower_3, "Flwey", "Sunflower", 4.2, 70, 15000),
    )

    val BankList = listOf(
        BankItem(1, "Ali An Nuur", "Bank BCA", "1234567890", "Bunga Adik"),
        BankItem(2, "Ali An Nuur", "Bank BNI", "1234567890", "Flower WwW"),
        BankItem(3, "Ali An Nuur", "Bank Mandiri", "1234567890", "Flwey"),
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
            id = 1,
            title = "How to do a promotion properly",
            description = "Learn how to manage your promotion to increase sale.",
            imageRes = R.drawable.tips_1
        ),
        TipsItem(
            id = 2,
            title = "How to present your product",
            description = "Learn how to present your product to increase customer engagement.",
            imageRes = R.drawable.tips_2
        ),
        TipsItem(
            id = 3,
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

    val accountItem: List<AccountList> = listOf(
        AccountList("Edit Store Profile", R.drawable.editprofile),
        AccountList("Address", R.drawable.map),
        AccountList("Language", R.drawable.language),
    )

    val generalItem: List<AccountList> = listOf(
        AccountList("Help Center", R.drawable.help),
        AccountList("Privacy", R.drawable.privacy),
        AccountList("Security", R.drawable.security),
        AccountList("Feedback", R.drawable.feedback),
//        AccountList("Logout", R.drawable.logout),
    )

}
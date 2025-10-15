package com.course.fleupart.ui.common

import com.course.fleupart.data.model.remote.*

object OrderDummyData {

    val dummyStore = OrderStore(
        id = "01e9dcd8-56d5-456f-9d9c-94bd2dda2dcc",
        name = "Rental Pes",
        picture = null,
        balance = "0.00",
        logo = null,
        phone = "09876766",
        updatedAt = "2025-03-05T15:00:17.114Z",
        operationalHour = "07.00 - 09.00 WITA",
        operationalDay = "Senin, Selasa",
        description = "Menjual Bunga Imut",
        sellerId = "e79bf3bb-0ad8-4f4e-aa50-93ea171fc024",
        adminVerifiedAt = null
    )

    val dummyCategory = OrderCategory(
        id = "6a4b974a-f4d1-4462-8e02-da6ab81993c1",
        name = "Graduation"
    )

    val dummyPicture = listOf(
        PictureItem(
            id = "958c344f-0b30-4a92-80d3-f0de25b15e68",
            path = "https://udykufvgdmysypdcsnnp.supabase.co/storage/v1/object/public/Image/product/picture/09b8d6e2-ad0e-43f9-a9da-606aa0c804b7/1744695400616.png"
        )
    )

    val dummyProduct = OrderProduct(
        id = "09b8d6e2-ad0e-43f9-a9da-606aa0c804b7",
        name = "Bunga Matahari",
        stock = 2,
        price = "20000.00",
        preOrder = false,
        description = "Bunga Cantik",
        arrangeTime = "30 - 60 Menit",
        point = 400,
        adminVerifiedAt = null,
        store = dummyStore,
        category = dummyCategory,
        picture = dummyPicture
    )

    val dummyOrderItem = OrderItemsItem(
        id = "85e6a755-8f29-4ad8-9c18-d9188326236f",
        quantity = 2,
        product = dummyProduct
    )

    val dummyPayment = OrderPayment(
        id = "f42d997b-ea28-436a-b53f-4568c087ee1c",
        createdAt = "2025-07-08T11:32:30.650Z",
        status = "expire",
        methode = "qris",
        successAt = null,
        orderId = "595cb73f-7d09-48f2-9564-18c98cc21f97",
        qrisUrl = "https://api.sandbox.midtrans.com/v2/qris/c58cd36d-fffe-407c-921e-d505d5db7eea/qr-code",
        qrisExpiredAt = "2025-07-08T18:47:26.000Z"
    )

    // New Orders (created status)
    val newOrdersDummy = listOf(
        OrderDataItem(
            id = "595cb73f-7d09-48f2-9564-18c98cc21f97",
            note = "Pesanan untuk wisuda anak",
            createdAt = "2025-07-08T11:32:21.615Z",
            total = 55000,
            point = 800,
            takenDate = "2025-08-08T08:47:37.000Z",
            takenMethod = "delivery",
            status = "created",
            addressId = "860541d4-5f93-4379-9b5d-e59e19750580",
            orderItems = listOf(dummyOrderItem),
            payment = dummyPayment
        ),
        OrderDataItem(
            id = "6713e3a2-7d61-475b-89a9-527540f4f087",
            note = "Tolong dibuat cantik ya",
            createdAt = "2025-07-07T12:30:00.241Z",
            total = 75000,
            point = 1000,
            takenDate = "2025-08-09T10:00:00.000Z",
            takenMethod = "pickup",
            status = "created",
            addressId = "860541d4-5f93-4379-9b5d-e59e19750580",
            orderItems = listOf(dummyOrderItem.copy(quantity = 3)),
            payment = dummyPayment.copy(status = "paid")
        )
    )

    // Process Orders (processing status)
    val processOrdersDummy = listOf(
        OrderDataItem(
            id = "process-001",
            note = "Sedang dalam proses pembuatan",
            createdAt = "2025-07-08T09:00:00.000Z",
            total = 100000,
            point = 1200,
            takenDate = "2025-08-10T14:30:00.000Z",
            takenMethod = "delivery",
            status = "processing",
            addressId = "860541d4-5f93-4379-9b5d-e59e19750580",
            orderItems = listOf(dummyOrderItem.copy(quantity = 4)),
            payment = dummyPayment.copy(status = "paid")
        )
    )

    // Pickup Orders (ready_pickup status)
    val pickupOrdersDummy = listOf(
        OrderDataItem(
            id = "pickup-001",
            note = "Siap untuk diambil",
            createdAt = "2025-07-07T08:00:00.000Z",
            total = 80000,
            point = 900,
            takenDate = "2025-08-11T16:00:00.000Z",
            takenMethod = "pickup",
            status = "ready_pickup",
            addressId = "860541d4-5f93-4379-9b5d-e59e19750580",
            orderItems = listOf(dummyOrderItem.copy(quantity = 3)),
            payment = dummyPayment.copy(status = "paid")
        )
    )

    // Delivery Orders (on_delivery status)
    val deliveryOrdersDummy = listOf(
        OrderDataItem(
            id = "delivery-001",
            note = "Sedang dalam perjalanan",
            createdAt = "2025-07-06T07:00:00.000Z",
            total = 120000,
            point = 1500,
            takenDate = "2025-08-12T18:00:00.000Z",
            takenMethod = "delivery",
            status = "on_delivery",
            addressId = "860541d4-5f93-4379-9b5d-e59e19750580",
            orderItems = listOf(dummyOrderItem.copy(quantity = 5)),
            payment = dummyPayment.copy(status = "paid")
        )
    )

    // Completed Orders
    val completedOrdersDummy = listOf(
        OrderDataItem(
            id = "4bf6ec23-9a2e-4c67-9681-318cb9771358",
            note = "Terima kasih, sangat puas!",
            createdAt = "2025-07-05T12:34:59.847Z",
            total = 55000,
            point = 800,
            takenDate = "2025-08-05T08:47:37.000Z",
            takenMethod = "delivery",
            status = "completed",
            addressId = "860541d4-5f93-4379-9b5d-e59e19750580",
            orderItems = listOf(dummyOrderItem),
            payment = dummyPayment.copy(status = "paid")
        ),
        OrderDataItem(
            id = "completed-002",
            note = "Bunga sangat fresh dan indah",
            createdAt = "2025-07-04T15:20:00.000Z",
            total = 95000,
            point = 1100,
            takenDate = "2025-08-04T12:00:00.000Z",
            takenMethod = "pickup",
            status = "completed",
            addressId = "860541d4-5f93-4379-9b5d-e59e19750580",
            orderItems = listOf(dummyOrderItem.copy(quantity = 4)),
            payment = dummyPayment.copy(status = "paid")
        )
    )

    // FilteredOrdersData dummy
    val filteredOrdersDataDummy = FilteredOrdersData(
        newOrders = newOrdersDummy,
        processOrders = processOrdersDummy,
        pickupOrders = pickupOrdersDummy,
        deliveryOrders = deliveryOrdersDummy,
        completedOrders = completedOrdersDummy,
        completedPaidOrders = completedOrdersDummy,
        completedUnpaidOrders = completedOrdersDummy,
    )
}

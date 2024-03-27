package com.rendox.grocerygenius.data.grocery

import com.rendox.grocerygenius.data.model.asExternalModel
import com.rendox.grocerygenius.database.grocery.GroceryDao
import com.rendox.grocerygenius.database.grocery.GroceryEntity
import com.rendox.grocerygenius.model.Grocery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GroceryRepositoryImpl @Inject constructor(
    private val groceryDao: GroceryDao,
) : GroceryRepository {
    override suspend fun addGroceryToList(
        productId: Int,
        listId: Int,
        description: String?,
        purchased: Boolean,
        purchasedLastModified: Long,
    ) {
        groceryDao.insertGrocery(
            GroceryEntity(
                productId = productId,
                groceryListId = listId,
                description = description,
                purchased = purchased,
                purchasedLastModified = purchasedLastModified,
            )
        )
    }

    override suspend fun insertProductAndGrocery(
        name: String,
        iconId: Int?,
        categoryId: Int?,
        groceryListId: Int,
        description: String?,
        purchased: Boolean,
        purchasedLastModified: Long,
        productIsDeletable: Boolean,
    ) {
        groceryDao.insertProductAndGrocery(
            name = name,
            iconId = iconId,
            categoryId = categoryId,
            groceryListId = groceryListId,
            description = description,
            purchased = purchased,
            purchasedLastModified = purchasedLastModified,
            productIsDeletable = productIsDeletable,
        )
    }

    override fun getGroceriesFromList(listId: Int): Flow<List<Grocery>> {
        return groceryDao.getGroceriesFromList(listId).map { combinedGroceries ->
            combinedGroceries.map { combinedGrocery ->
                combinedGrocery.asExternalModel()
            }
        }
    }

    override suspend fun getGrocery(productId: Int, listId: Int): Grocery? {
        return groceryDao.getGrocery(productId, listId)?.asExternalModel()
    }

    override suspend fun updatePurchased(
        productId: Int,
        listId: Int,
        purchased: Boolean,
        purchasedLastModified: Long,
    ) {
        groceryDao.updatePurchased(
            productId,
            listId,
            purchased,
            purchasedLastModified,
        )
    }

    override suspend fun updateDescription(productId: Int, listId: Int, description: String?) {
        groceryDao.updateDescription(productId, listId, description)
    }

    override suspend fun removeGroceryFromList(productId: Int, listId: Int) {
        groceryDao.deleteGrocery(productId, listId)
    }
}
package com.rendox.grocerygenius.data.category

import com.rendox.grocerygenius.data.Synchronizer
import com.rendox.grocerygenius.data.changeListSync
import com.rendox.grocerygenius.data.model.asEntity
import com.rendox.grocerygenius.data.model.asExternalModel
import com.rendox.grocerygenius.database.category.CategoryDao
import com.rendox.grocerygenius.model.Category
import com.rendox.grocerygenius.network.category.CategoryNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val categoryNetworkDataSource: CategoryNetworkDataSource,
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { categories ->
            categories.map { categoryEntity ->
                categoryEntity.asExternalModel()
            }
        }
    }

    override suspend fun syncWith(synchronizer: Synchronizer) = synchronizer.changeListSync(
        checkIfExistingDataIsEmpty = {
            categoryDao.getAllCategories().first().isEmpty()
        },
        prepopulateWithInitialData = {
            val categories = categoryNetworkDataSource.getAllCategories()
            categoryDao.upsertCategories(categories.map { it.asEntity() })
        },
        versionReader = { it.categoryVersion },
        changeListFetcher = { currentVersion ->
            categoryNetworkDataSource.getCategoryChangeList(after = currentVersion)
        },
        versionUpdater = { latestVersion ->
            copy(categoryVersion = latestVersion)
        },
        modelDeleter = { categoryIds ->
            categoryDao.deleteCategories(categoryIds)
        },
        modelUpdater = { changedIds ->
            val networkCategories =
                categoryNetworkDataSource.getCategoriesByIds(ids = changedIds)
            categoryDao.upsertCategories(networkCategories.map { it.asEntity() })
        },
    )
}
package com.rendox.grocerygenius.data.model

import com.rendox.grocerygenius.database.category.CategoryEntity
import com.rendox.grocerygenius.model.Category
import com.rendox.grocerygenius.network.model.CategoryNetwork

fun CategoryEntity.asExternalModel() = Category(
    id = id,
    name = name,
    sortingPriority = sortingPriority,
    defaultSortingPriority = defaultSortingPriority,
)

fun Category.asEntity() = CategoryEntity(
    id = id,
    name = name,
    sortingPriority = sortingPriority,
    defaultSortingPriority = defaultSortingPriority,
)

fun CategoryNetwork.asEntity() = CategoryEntity(
    id = id,
    name = name,
    sortingPriority = sortingPriority,
    defaultSortingPriority = sortingPriority,
)
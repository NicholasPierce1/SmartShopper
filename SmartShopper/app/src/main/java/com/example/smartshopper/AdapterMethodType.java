package com.example.smartshopper;

// purpose: to enumerate the different/various adapter methods that'll be invoked via broker
// each  case will require the fabrication/appending of a new broker interface handler
public enum AdapterMethodType {
    validateIfBarcodeExist,
    validateNameForVendorIsUnique,
    createItem,
    updateItem,
    deleteItem,
    findItemBySearch,
    findAdminByLogin,
    findAdminByEmpId,
    addAdmin,
    deleteAdmin,
    updateAdmin,
    initializeDepartments,
    getAllStores
}

package com.example.ecommerce.helper;

public final class URLContract {
    private static final String BASE_URL = "https://trustbuy.000webhostapp.com/api";
    //Folder
    private static final String IMAGE_FOLDER_NAME = "images";
    private static final String PROFILE_FOLDER_NAME = "profiles";
    private static final String PRODUCTS_FOLDER_NAME = "products";
    private static final String CATEGORIES_FOLDER_NAME = "categories";
    //Api
    public static final String LOGIN_URL = BASE_URL + "/login.php";
    public static final String REGISTER_URL = BASE_URL + "/register.php";
    public static final String GET_USER_DATA_URL = BASE_URL + "/getuserdata.php";
    public static final String UPDATE_USER_DATA_URL = BASE_URL + "/updateuserdata.php";
    public static final String GET_ALL_CATEGORIES_URL = BASE_URL + "/getallcategories.php";
    public static final String GET_PRODUCTS_BY_CATEGORY_URL = BASE_URL + "/getproductsbycategory.php";

    //Images
    public static final String PROFILE_PIC_URL = BASE_URL + "/" + IMAGE_FOLDER_NAME + "/" + PROFILE_FOLDER_NAME;
    public static final String PRODUCT_PIC_URL = BASE_URL + "/" + IMAGE_FOLDER_NAME + "/" + PRODUCTS_FOLDER_NAME;
    public static final String CATEGORY_PIC_URL = BASE_URL + "/" + IMAGE_FOLDER_NAME + "/" + CATEGORIES_FOLDER_NAME;
}

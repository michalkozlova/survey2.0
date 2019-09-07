package michal.edu.survey.Models;

public class Store {

    public static final int STORE_RETAIL = 0;
    public static final int STORE_RESTAURANT = 1;

    private String storeName;
    private int storeType;
    private String storeID;
//    private ArrayList<Branch> branches;
//    private ArrayList<Section> questionnaire;
    private Boolean hasBranches;

    public Store() {
    }

    public Store(String storeName, int storeType, String storeID, Boolean hasBranches) {
        this.storeName = storeName;
        this.storeType = storeType;
        this.storeID = storeID;
        this.hasBranches = hasBranches;
    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public Boolean getHasBranches() {
        return hasBranches;
    }

    public void setHasBranches(Boolean hasBranches) {
        this.hasBranches = hasBranches;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeName='" + storeName + '\'' +
                ", storeType=" + storeType +
                ", storeID='" + storeID + '\'' +
//                ", branches=" + branches +
//                ", questionnaire=" + questionnaire +
                '}';
    }

}

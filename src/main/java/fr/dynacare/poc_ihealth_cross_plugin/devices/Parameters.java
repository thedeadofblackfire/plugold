package fr.dynacare.poc_ihealth_cross_plugin.devices;

public class Parameters {
    public int mUserId;
    public UserInfo mUserInfo;
    public AMParams mAmParams;

    public Parameters(){
        mUserInfo = new UserInfo();
        mAmParams = new AMParams();
    }
}

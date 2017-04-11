
package fr.dynacare.poc_ihealth_cross_plugin.models.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityEachDatum {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("stepsize")
    @Expose
    private String stepsize;
    @SerializedName("step")
    @Expose
    private Integer step;
    @SerializedName("calorie")
    @Expose
    private Integer calorie;
    @SerializedName("dataID")
    @Expose
    private String dataID;

    /**
     * 
     * @return
     *     The time
     */
    public String getTime() {
        return time;
    }

    /**
     * 
     * @param time
     *     The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     * @return
     *     The stepsize
     */
    public String getStepsize() {
        return stepsize;
    }

    /**
     * 
     * @param stepsize
     *     The stepsize
     */
    public void setStepsize(String stepsize) {
        this.stepsize = stepsize;
    }

    /**
     * 
     * @return
     *     The step
     */
    public Integer getStep() {
        return step;
    }

    /**
     * 
     * @param step
     *     The step
     */
    public void setStep(Integer step) {
        this.step = step;
    }

    /**
     * 
     * @return
     *     The calorie
     */
    public Integer getCalorie() {
        return calorie;
    }

    /**
     * 
     * @param calorie
     *     The calorie
     */
    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    /**
     * 
     * @return
     *     The dataID
     */
    public String getDataID() {
        return dataID;
    }

    /**
     * 
     * @param dataID
     *     The dataID
     */
    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

}

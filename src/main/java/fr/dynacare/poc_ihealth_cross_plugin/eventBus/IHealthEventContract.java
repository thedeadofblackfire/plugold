package fr.dynacare.poc_ihealth_cross_plugin.eventBus;

/**
 * Created by guillaumeboufflers on 06/07/2016.
 */
public interface IHealthEventContract {
    public void onEvent(IHealthEvent event);
}

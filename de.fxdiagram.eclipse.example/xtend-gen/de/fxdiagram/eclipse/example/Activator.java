package de.fxdiagram.eclipse.example;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

@SuppressWarnings("all")
public class Activator extends AbstractUIPlugin {
  public final static String PLUGIN_ID = "de.fxdiagram.eclipse";
  
  private static Activator plugin;
  
  @Override
  public void start(final BundleContext context) throws Exception {
    super.start(context);
    Activator.plugin = this;
  }
  
  @Override
  public void stop(final BundleContext context) throws Exception {
    Activator.plugin = null;
    super.stop(context);
  }
  
  public static Activator getDefault() {
    return Activator.plugin;
  }
}

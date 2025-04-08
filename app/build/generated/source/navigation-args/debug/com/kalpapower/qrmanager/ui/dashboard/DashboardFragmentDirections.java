package com.kalpapower.qrmanager.ui.dashboard;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import com.kalpapower.qrmanager.R;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class DashboardFragmentDirections {
  private DashboardFragmentDirections() {
  }

  @NonNull
  public static ActionDashboardToProductDetail actionDashboardToProductDetail(
      @NonNull String serialNumber) {
    return new ActionDashboardToProductDetail(serialNumber);
  }

  public static class ActionDashboardToProductDetail implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionDashboardToProductDetail(@NonNull String serialNumber) {
      if (serialNumber == null) {
        throw new IllegalArgumentException("Argument \"serialNumber\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("serialNumber", serialNumber);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionDashboardToProductDetail setSerialNumber(@NonNull String serialNumber) {
      if (serialNumber == null) {
        throw new IllegalArgumentException("Argument \"serialNumber\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("serialNumber", serialNumber);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("serialNumber")) {
        String serialNumber = (String) arguments.get("serialNumber");
        __result.putString("serialNumber", serialNumber);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_dashboard_to_productDetail;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getSerialNumber() {
      return (String) arguments.get("serialNumber");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionDashboardToProductDetail that = (ActionDashboardToProductDetail) object;
      if (arguments.containsKey("serialNumber") != that.arguments.containsKey("serialNumber")) {
        return false;
      }
      if (getSerialNumber() != null ? !getSerialNumber().equals(that.getSerialNumber()) : that.getSerialNumber() != null) {
        return false;
      }
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + (getSerialNumber() != null ? getSerialNumber().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionDashboardToProductDetail(actionId=" + getActionId() + "){"
          + "serialNumber=" + getSerialNumber()
          + "}";
    }
  }
}

package com.kalpapower.qrmanager.ui.details;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import com.kalpapower.qrmanager.R;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class ProductDetailFragmentDirections {
  private ProductDetailFragmentDirections() {
  }

  @NonNull
  public static ActionProductDetailToReportFault actionProductDetailToReportFault(long productId) {
    return new ActionProductDetailToReportFault(productId);
  }

  public static class ActionProductDetailToReportFault implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionProductDetailToReportFault(long productId) {
      this.arguments.put("productId", productId);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionProductDetailToReportFault setProductId(long productId) {
      this.arguments.put("productId", productId);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("productId")) {
        long productId = (long) arguments.get("productId");
        __result.putLong("productId", productId);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_productDetail_to_reportFault;
    }

    @SuppressWarnings("unchecked")
    public long getProductId() {
      return (long) arguments.get("productId");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionProductDetailToReportFault that = (ActionProductDetailToReportFault) object;
      if (arguments.containsKey("productId") != that.arguments.containsKey("productId")) {
        return false;
      }
      if (getProductId() != that.getProductId()) {
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
      result = 31 * result + (int)(getProductId() ^ (getProductId() >>> 32));
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionProductDetailToReportFault(actionId=" + getActionId() + "){"
          + "productId=" + getProductId()
          + "}";
    }
  }
}

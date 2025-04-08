package com.kalpapower.qrmanager.ui.fault;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class ReportFaultFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private ReportFaultFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private ReportFaultFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static ReportFaultFragmentArgs fromBundle(@NonNull Bundle bundle) {
    ReportFaultFragmentArgs __result = new ReportFaultFragmentArgs();
    bundle.setClassLoader(ReportFaultFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("productId")) {
      long productId;
      productId = bundle.getLong("productId");
      __result.arguments.put("productId", productId);
    } else {
      throw new IllegalArgumentException("Required argument \"productId\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static ReportFaultFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    ReportFaultFragmentArgs __result = new ReportFaultFragmentArgs();
    if (savedStateHandle.contains("productId")) {
      long productId;
      productId = savedStateHandle.get("productId");
      __result.arguments.put("productId", productId);
    } else {
      throw new IllegalArgumentException("Required argument \"productId\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  public long getProductId() {
    return (long) arguments.get("productId");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("productId")) {
      long productId = (long) arguments.get("productId");
      __result.putLong("productId", productId);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("productId")) {
      long productId = (long) arguments.get("productId");
      __result.set("productId", productId);
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    ReportFaultFragmentArgs that = (ReportFaultFragmentArgs) object;
    if (arguments.containsKey("productId") != that.arguments.containsKey("productId")) {
      return false;
    }
    if (getProductId() != that.getProductId()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (int)(getProductId() ^ (getProductId() >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "ReportFaultFragmentArgs{"
        + "productId=" + getProductId()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull ReportFaultFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(long productId) {
      this.arguments.put("productId", productId);
    }

    @NonNull
    public ReportFaultFragmentArgs build() {
      ReportFaultFragmentArgs result = new ReportFaultFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setProductId(long productId) {
      this.arguments.put("productId", productId);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    public long getProductId() {
      return (long) arguments.get("productId");
    }
  }
}

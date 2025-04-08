package com.kalpapower.qrmanager.ui.details;

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

public class ProductDetailFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private ProductDetailFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private ProductDetailFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static ProductDetailFragmentArgs fromBundle(@NonNull Bundle bundle) {
    ProductDetailFragmentArgs __result = new ProductDetailFragmentArgs();
    bundle.setClassLoader(ProductDetailFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("serialNumber")) {
      String serialNumber;
      serialNumber = bundle.getString("serialNumber");
      if (serialNumber == null) {
        throw new IllegalArgumentException("Argument \"serialNumber\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("serialNumber", serialNumber);
    } else {
      throw new IllegalArgumentException("Required argument \"serialNumber\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static ProductDetailFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    ProductDetailFragmentArgs __result = new ProductDetailFragmentArgs();
    if (savedStateHandle.contains("serialNumber")) {
      String serialNumber;
      serialNumber = savedStateHandle.get("serialNumber");
      if (serialNumber == null) {
        throw new IllegalArgumentException("Argument \"serialNumber\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("serialNumber", serialNumber);
    } else {
      throw new IllegalArgumentException("Required argument \"serialNumber\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getSerialNumber() {
    return (String) arguments.get("serialNumber");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("serialNumber")) {
      String serialNumber = (String) arguments.get("serialNumber");
      __result.putString("serialNumber", serialNumber);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("serialNumber")) {
      String serialNumber = (String) arguments.get("serialNumber");
      __result.set("serialNumber", serialNumber);
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
    ProductDetailFragmentArgs that = (ProductDetailFragmentArgs) object;
    if (arguments.containsKey("serialNumber") != that.arguments.containsKey("serialNumber")) {
      return false;
    }
    if (getSerialNumber() != null ? !getSerialNumber().equals(that.getSerialNumber()) : that.getSerialNumber() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getSerialNumber() != null ? getSerialNumber().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ProductDetailFragmentArgs{"
        + "serialNumber=" + getSerialNumber()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull ProductDetailFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@NonNull String serialNumber) {
      if (serialNumber == null) {
        throw new IllegalArgumentException("Argument \"serialNumber\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("serialNumber", serialNumber);
    }

    @NonNull
    public ProductDetailFragmentArgs build() {
      ProductDetailFragmentArgs result = new ProductDetailFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setSerialNumber(@NonNull String serialNumber) {
      if (serialNumber == null) {
        throw new IllegalArgumentException("Argument \"serialNumber\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("serialNumber", serialNumber);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getSerialNumber() {
      return (String) arguments.get("serialNumber");
    }
  }
}

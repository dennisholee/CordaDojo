package io.forest.realestate.action;

import io.forest.realestate.states.PropertyDetails;

public interface PropertyAction<T> {

	PropertyDetails action(PropertyDetails propertyDetails, T obj);
}

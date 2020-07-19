package io.forest.realestate.schema;

import com.google.common.collect.ImmutableList;

import net.corda.core.schemas.MappedSchema;

public class PropertyDetailsSchemaV1 extends MappedSchema {

	public PropertyDetailsSchemaV1() {
		super(PropertyDetailsSchemaV1.class, 1, ImmutableList.of(PersistentPropertyDetails.class));
	}


}

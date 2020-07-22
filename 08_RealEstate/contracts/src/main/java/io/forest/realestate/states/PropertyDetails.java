package io.forest.realestate.states;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

import io.forest.realestate.contracts.PropertyContract;
import io.forest.realestate.schema.PersistentPropertyDetails;
import io.forest.realestate.schema.PropertyDetailsSchemaV1;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;
import net.corda.core.serialization.ConstructorForDeserialization;

@BelongsToContract(PropertyContract.class)
@Value
@Builder(toBuilder = true)
public class PropertyDetails implements LinearState, QueryableState {

	private final int propertyId;

	@NonNull
	private final String propertyAddress;

	private final int propertyPrice;

	private final int buyerId;

	private final int sellerId;

	private final boolean isMortgageApproved;

	private final boolean isSurveyorApproved;

	private final Party owner;

	private final String description;

	private final String updatedBy;

	private final String updatedTime;

	private final UniqueIdentifier linearId;

	public PropertyDetails(int propertyId, String propertyAddress, int propertyPrice, int buyerId, int sellerId,
			boolean isMortgageApproved, boolean isSurveyorApproved, Party owner, String description, String updatedBy,
			String updatedTime) {
		this(propertyId, propertyAddress, propertyPrice, buyerId, sellerId, isMortgageApproved, isSurveyorApproved,
				owner, description, updatedBy, updatedTime, new UniqueIdentifier());

	}

	@ConstructorForDeserialization
	public PropertyDetails(int propertyId, String propertyAddress, int propertyPrice, int buyerId, int sellerId,
			boolean isMortgageApproved, boolean isSurveyorApproved, Party owner, String description, String updatedBy,
			String updatedTime, UniqueIdentifier linearId) {
		this.propertyId = propertyId;
		this.owner = owner;
		this.propertyAddress = propertyAddress;
		this.propertyPrice = propertyPrice;
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.isMortgageApproved = isMortgageApproved;
		this.isSurveyorApproved = isSurveyorApproved;
		this.description = description;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
		this.linearId = linearId;
	}

	@Override
	public List<AbstractParty> getParticipants() {
		return ImmutableList.of(this.owner);
	}

	@Override
	public PersistentState generateMappedObject(MappedSchema mappedSchema) {
		if (!(mappedSchema instanceof PropertyDetailsSchemaV1)) {
			throw new IllegalArgumentException("Unrecognised schema $schema");
		}

		return new PersistentPropertyDetails(this.propertyId, this.propertyAddress, this.propertyPrice, this.buyerId,
				this.linearId.getId());
	}

	@Override
	public Iterable<MappedSchema> supportedSchemas() {
		return ImmutableList.of(new PropertyDetailsSchemaV1());
	}
}

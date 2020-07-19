package io.forest.realestate.schema;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.corda.core.schemas.PersistentState;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "land_registry_states")
public class PersistentPropertyDetails extends PersistentState {

	@Getter
	@Column(name = "propertyId")
	private int propertyId;

	@Getter
	@Column(name = "propertyAddress")
	private String propertyAddress;

	@Getter
	@Column(name = "propertyPrice")
	private int propertyPrice;

	@Getter
	@Column(name = "buyerId")
	private int buyerId;

	@Getter
	@Column(name = "linear_id")
	private UUID linearId;
}

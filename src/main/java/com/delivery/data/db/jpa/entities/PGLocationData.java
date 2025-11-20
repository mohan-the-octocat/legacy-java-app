package com.delivery.data.db.jpa.entities;

import com.delivery.core.domain.Identity;
import com.delivery.core.domain.PGLocation;
import lombok.*;

import javax.persistence.*;

import static com.delivery.data.db.jpa.entities.IdConverter.convertId;

@AllArgsConstructor
@Entity(name = "pg_location")
@Getter
@NoArgsConstructor
@Setter
@Table(name = "pg_location")
public class PGLocationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String managerContact;

    public static PGLocationData from(PGLocation pgLocation) {
        return new PGLocationData(
                convertId(pgLocation.getId()),
                pgLocation.getName(),
                pgLocation.getAddress(),
                pgLocation.getManagerContact()
        );
    }

    public PGLocation fromThis() {
        return new PGLocation(
                new Identity(id),
                name,
                address,
                managerContact
        );
    }
}

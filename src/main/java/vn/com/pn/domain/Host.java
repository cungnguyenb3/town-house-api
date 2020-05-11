package vn.com.pn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="hosts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Host extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String name;

    @NonNull
    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostCategory hostCategory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "host_room_type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostRoomType hostRoomType;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostCity hostCity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cancellation_policy_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostCancellationPolicy hostCancellationPolicy;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "procedures_check_in_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProcedureCheckIn procedureCheckIn;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "currency_unit_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CurrencyUnit currencyUnit;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "hosts_rules",
            joinColumns = @JoinColumn(name = "host_id"),
            inverseJoinColumns = @JoinColumn(name = "rule_id"))
    private Set<Rule> rules = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "languages_communications",
            joinColumns = @JoinColumn(name = "host_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private Set<Language> languages = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "hosts_date_can_not_booking",
            joinColumns = @JoinColumn(name = "host_id"),
            inverseJoinColumns = @JoinColumn(name = "date_can_not_booking_id"))
    private Set<DateCanNotBooking> dateCanNotBookings = new HashSet<>();

//    @OneToMany(mappedBy = "host", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<HostImage> hostImages = new HashSet<>();

    private String address;
    private String latitude;
    private String longitude;
    private Integer bedroomCount;
    private Integer bed;
    private Integer bathroomCount;
    private boolean availabilityType;
    private boolean refundType;
    private boolean status;
    private boolean isApproved;
    private Float stars;
    private Integer totalReview;

    private Long standardPriceMondayToThursday;
    private Long standardPriceFridayToSunday;

    private Long cleaningCosts;
    private Long adultCostsIncrease;
    private Long childrenCostsIncrease;
    private Byte serviceChargePercent;

    private Short weeklyDiscount;
    private Short monthlyDiscount;
    private Short earlyBirdDiscount;
    private Short lastMinuteDiscount;
    private Short daysPriorToBooking;

    private Integer numberOfStandardGuest;
    private Integer numberOfMaximumGuest;

    private Integer numberOfAdultGuest;
    private Integer numberOfChildrenGuest;
    private Integer numberOfInfantGuest;

    private boolean isAddChildrenAndInfantIntoMaximumGuest;

    private Integer numberOfMinimumNight;
    private Integer numberOfMaximumNight;

    private Integer acreage;

    private LocalTime earliestCheckIn;
    private LocalTime latestCheckIn;
    private LocalTime checkOutTime;

    private Integer JapaneseCushion;
    private Integer sofa;
    private Integer bunk;
    private Integer kitchenCount;

    @Lob
    private String checkInInstructions;

    @Lob
    private String usingConvenientInstructions;

    private boolean isHasGardenView;
    private boolean isHasParkView;
    private boolean isHasForestView;
    private boolean isHasElevator;
    private boolean isHasStair;
    private boolean isHasMeeting;
    private boolean isHasGarbageCollectionArea;
    private boolean isHas24HourReception;
    private boolean isHasSmokingArea;
    private boolean isHasOutdoorParkingLot;
    private boolean isHasIndoorParkingLot;
    private boolean isHasSharedCarPark;
    private boolean isHasCarPark;
    private boolean isHasBilliard;
    private boolean isHasPingPong;
    private boolean isHasFishing;
    private boolean isHasTennisCourse;
    private boolean isHasTerrace;
    private boolean isHasBBQAreaWithDiningTable;
    private boolean isHasChildrenPlayArea;
    private boolean isHasGarden;
    private boolean isHasInfinityPool;
    private boolean isHasOutdoorPool;
    private boolean isHasIndoorSwimmingPool;
    private boolean isHasGym;
    private boolean isHasYoga;
    private boolean isHasSauna;
    private boolean isHasSpa;
    private boolean isHasHairdressers;
    private boolean isHasClinic;
    private boolean isHasPharmacies;
    private boolean isHasCoffeeShop;
    private boolean isHasBar;
    private boolean isHasRestaurant;
    private boolean isHasShoppingMall;
    private boolean isHasSouvenirStore;
    private boolean isNearTheGolfCourse;
    private boolean isCloseToTheBusStop;
    private boolean isNearTheMuseum;
    private boolean isNearTheATM;
    private boolean isNearThePark;
    private boolean isFreeBreakfast;
    private boolean isAirportTransfer;
    private boolean isHasVehicleRental;
    private boolean isHasFreeBicycles;
    private boolean isHasTicketingService;
    private boolean isHasLaundry;
    private boolean isHasSubmitItemsBeforeCheckIn;
}
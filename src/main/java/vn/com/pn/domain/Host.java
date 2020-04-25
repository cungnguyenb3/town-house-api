package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="hosts")
public class Host extends DateAudit implements Serializable {
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

    private String address;

    private String latitude;

    private String longitude;

    @Column(name = "bedroom_count")
    private Integer bedroomCount;

    private Integer bed;

    @Column(name = "bathroom_count")
    private Integer bathroomCount;

    @Column(name = "availability_type")
    private boolean availabilityType;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    private BigDecimal price;

    @Column(name = "price_type")
    private boolean priceType;

//    private Long standardPriceMondayToThursday;
//    private Long standardPriceFridayToSunday;

//    private Integer acreage;

//    private Integer singleBed;
//    private Integer doubleSmallBed;
//    private Integer doubleBigBed;
//    private Integer superBigBed;
//    private Integer JapaneseCushion;
//    private Integer sofa;
//    private Integer bunk;
//    private Integer privateBathroom;
//    private Integer sharedBathroom;
//    private Integer kitchenCount;
//    private Integer commonKitchen;
//    private Integer sharedKitchen;
//    private Byte canSmoke;
//    private Byte canHasPet;
//    private Byte canParty;
//    private Byte canCooking;
//    private Byte canCommercialPhotography;


    //    private boolean isHasWifi;
//    private boolean isHasInternet;
//    private boolean isHasAirConditioning;
//    private boolean isHasFan;
//    private boolean isHasPrivateWay;
//    private boolean isHasDryer;
//    private boolean isHasHeater;
//    private boolean isHasWashingMachine;
//    private boolean isHasWashingPowderOrWashingLiquid;
//    private boolean isHasClothesDryer;
//    private boolean isSuitableForChildren;
//    private boolean isHasBabyCot;
//    private boolean isHasDesk;
//    private boolean isHasIron;
//    private boolean isHasFridge;
//    private boolean isHasClothesHorse;
//    private boolean isHasSandals;
//    private boolean isHasCleaningTools;
//    private boolean isHasWindow;
//    private boolean isHasSofa;
//    private boolean isHas220VPowerOutlet;
//    private boolean isHas110VPowerOutlet;
//    private boolean isHasFreeDrinkWater;
//    private boolean isHasWardrobe;
//    private boolean isHasClothHook;
//    private boolean isHasAdditionalCushion;
//    private boolean isHasMakeupTable;
//    private boolean isHasBathtub;
//    private boolean isHasShowerCubicle;
//    private boolean isHasToilet;
//    private boolean isHasBathroomHeatingLamps;
//    private boolean isHasTower;
//    private boolean isHasShampoosAndConditioners;
//    private boolean isHasShowerCap;
//    private boolean isHasFreeToiletries;
//    private boolean isHasHose;
//    private boolean isHasElectricHobOrInductionHob;
//    private boolean isHasGasStove;
//    private boolean isHasBBQGrill;
//    private boolean isHasCooker;
//    private boolean isHasKettle;
//    private boolean isHasBasicCookingSpice;
//    private boolean isHasOvenMitts;
//    private boolean isHasMicrowave;
//    private boolean isHasToaster;
//    private boolean isHasCookingUtensils;
//    private boolean isHasDishwasher;
//    private boolean isHasCharcoal;
//    private boolean isHasDishes;
//    private boolean isHasCup;
//    private boolean isHasDiningTable;
//    private boolean isHasTissue;
//    private boolean isHasFireAlarmBox;
//    private boolean isHasFireExtinguisher;
//    private boolean isHasAntiTheftLock;
//    private boolean isHasSafeVault;
//    private boolean isHasBalcony;
//    private boolean isHasPrivateBBQArea;
//    private boolean isHasPrivateYardOrGarden;
//    private boolean isHasPrivatePool;
//    private boolean isHasPrivateParking;
//    private boolean isHasMassageChair;
//    private boolean isHasTantraChair;
//    private boolean isHasSmartHome;
//    private boolean isHasHealthCareBox;
//    private boolean isHasCoffeeMaker;
//    private boolean isHasSmartTVOrInternetTV;
//    private boolean isHasKaraoke;
//    private boolean isHasJacuzzi;
//    private boolean isHasSpeaker;
//    private boolean isHasMosquitoNetting;
//    private boolean isHasPlaystation;
//    private boolean isHasXBox;
//    private boolean isHasHeater;
//    private boolean isHasBayView;
//    private boolean isHasCityView;
//    private boolean isHasSeaView;
//    private boolean isHasViewPort;
//    private boolean isHasRiverView;
//    private boolean isHasLakeView;
//    private boolean isHasMountainView;
//    private boolean isHasPoolView;
//    private boolean isHasGardenView;
//    private boolean isHasParkView;
//    private boolean isHasForestView;
//    private boolean isHasElevator;
//    private boolean isHasStair;
//    private boolean isHasMeeting;
//    private boolean isHasGarbageCollectionArea;
//    private boolean isHas24HourReception;
//    private boolean isHasSmokingArea;
//    private boolean isHasOutdoorParkingLot;
//    private boolean isHasIndoorParkingLot;
//    private boolean isHasSharedCarPark;
//    private boolean isHasCarPark;
//    private boolean isHasBilliard;
//    private boolean isHasPingPong;
//    private boolean isHasFishing;
//    private boolean isHasTennisCourse;
//    private boolean isHasTerrace;
//    private boolean isHasBBQAreaWithDiningTable;
//    private boolean isHasChildrenPlayArea;
//    private boolean isHasGarden;
//    private boolean isHasInfinityPool;
//    private boolean isHasOutdoorPool;
//    private boolean isHasIndoorSwimmingPool;
//    private boolean isHasGym;
//    private boolean isHasYoga;
//    private boolean isHasSauna;
//    private boolean isHasSpa;
//    private boolean isHasHairdressers;
//    private boolean isHasClinic;
//    private boolean isHasPharmacies;
//    private boolean isHasCoffeeShop;
//    private boolean isHasBar;
//    private boolean isHasRestaurant;
//    private boolean isHasShoppingMall;
//    private boolean isHasSouvenirStore;
//    private boolean isNearTheGolfCourse;
//    private boolean isCloseToTheBusStop;
//    private boolean isNearTheMuseum;
//    private boolean isNearTheATM;
//    private boolean isNearThePark;
//    private boolean isFreeBreakfast;
//    private boolean isAirportTransfer;
//    private boolean isHasVehicleRental;
//    private boolean isHasFreeBicycles;
//    private boolean isHasTicketingService;
//    private boolean isHasLaundry;
//    private boolean isHasSubmitItemsBeforeCheckIn;

//    private Long cleaningCosts;
//    private Long adultCostsIncrease;
//    private Long childrenCostsIncrease;

//    private Short weeklyDiscount;
//    private Short monthlyDiscount;
//    private Short earlyBirdDiscount;
//    private Short LastMinuteDiscount;
//    private Short daysPriorToBooking;
//
//    private Integer numberOfAdultGuest;
//    private Integer numberOfChildrenGuest;
//    private Integer numberOfInfantGuest;
//    private boolean isAddChildrenAndInfantIntoMaximumGuest;

//    private Integer numberOfStandardGuest;
//    private Integer numberOfMaximumGuest;
//    private Short numberOfMinimumNight;
//    private Short numberOfMaximumNight;

    private String minimumStay;

    @Column(name = "minimum_stay_type")
    private boolean minimumStayType;

    @Column(name = "refund_type")
    private boolean refundType;

    private boolean status;

    private float star;

    @Column(name = "total_review")
    private Integer totalReview;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(name = "host_rules",
//            joinColumns = @JoinColumn(name = "host_id"),
//            inverseJoinColumns = @JoinColumn(name = "host_rule_id"))
//    private Set<HostRule> hostRules = new HashSet<>();

}

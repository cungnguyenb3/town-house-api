package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;
import vn.com.pn.domain.DateCanNotBooking;
import vn.com.pn.domain.HostImage;
import vn.com.pn.domain.Language;
import vn.com.pn.domain.Rule;

import javax.persistence.Column;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
public class HostRequest extends BaseRequest{
    private String name;

    private String description;

    private String hostCategoryId;

    private String hostRoomTypeId;

    private String hostCityId;

    private String hostCancellationPolicyId;

    private String procedureCheckInId;

    private String currencyUnitId;

    private Set<String> ruleIds;

    private Set<String> languageIds;

    private String address;

    private String latitude;

    private String longitude;

    private String bedroomCount;

    private String bed;

    private String bathroomCount;

    private String price;

    private String standardPriceMondayToThursday;

    private String standardPriceFridayToSunday;

    private String cleaningCosts;

    private String adultCostsIncrease;

    private String childrenCostsIncrease;

    private String serviceChargePercent;

    private String weeklyDiscount;

    private String monthlyDiscount;

    private String earlyBirdDiscount;

    private String lastMinuteDiscount;

    private String daysPriorToBooking;

    private String numberOfStandardGuest;

    private String numberOfMaximumGuest;

    private String numberOfAdultGuest;

    private String numberOfChildrenGuest;

    private String numberOfInfantGuest;

    private String isAddChildrenAndInfantIntoMaximumGuest;

    private String numberOfMinimumNight;

    private String numberOfMaximumNight;

    private String acreage;

    private String earliestCheckIn;
    private String latestCheckIn;
    private String checkOutTime;

    private String JapaneseCushion;
    private String sofa;
    private String bunk;
    private String kitchenCount;

    private String checkInInstructions;

    private String usingConvenientInstructions;

    private String isHasGardenView;
    private String isHasParkView;
    private String isHasForestView;
    private String isHasElevator;
    private String isHasStair;
    private String isHasMeeting;
    private String isHasGarbageCollectionArea;
    private String isHas24HourReception;
    private String isHasSmokingArea;
    private String isHasOutdoorParkingLot;
    private String isHasIndoorParkingLot;
    private String isHasSharedCarPark;
    private String isHasCarPark;
    private String isHasBilliard;
    private String isHasPingPong;
    private String isHasFishing;
    private String isHasTennisCourse;
    private String isHasTerrace;
    private String isHasBBQAreaWithDiningTable;
    private String isHasChildrenPlayArea;
    private String isHasGarden;
    private String isHasInfinityPool;
    private String isHasOutdoorPool;
    private String isHasIndoorSwimmingPool;
    private String isHasGym;
    private String isHasYoga;
    private String isHasSauna;
    private String isHasSpa;
    private String isHasHairdressers;
    private String isHasClinic;
    private String isHasPharmacies;
    private String isHasCoffeeShop;
    private String isHasBar;
    private String isHasRestaurant;
    private String isHasShoppingMall;
    private String isHasSouvenirStore;
    private String isNearTheGolfCourse;
    private String isCloseToTheBusStop;
    private String isNearTheMuseum;
    private String isNearTheATM;
    private String isNearThePark;
    private String isFreeBreakfast;
    private String isAirportTransfer;
    private String isHasVehicleRental;
    private String isHasFreeBicycles;
    private String isHasTicketingService;
    private String isHasLaundry;
    private String isHasSubmitItemsBeforeCheckIn;
}

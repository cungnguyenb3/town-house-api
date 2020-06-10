package vn.com.pn.screen.m002Host.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m002Host.dto.HostDTO;
import vn.com.pn.screen.m002Host.entity.HostDiscount;
import vn.com.pn.screen.m006HostCategory.dto.HostDiscountDTO;
import vn.com.pn.screen.m002Host.dto.HostUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.config.ScheduledConfig;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.m007HostRoomType.entity.HostRoomType;
import vn.com.pn.screen.m008CurrencyUnit.entity.CurrencyUnit;
import vn.com.pn.screen.m008CurrencyUnit.repository.CurrencyUnitRepository;
import vn.com.pn.screen.m002Host.repository.HostRepository;
import vn.com.pn.screen.m002Host.repository.HostRepositoryCustom;
import vn.com.pn.screen.m009HostProcedureCheckIn.entity.ProcedureCheckIn;
import vn.com.pn.screen.m010HostCancallationPolicy.entity.HostCancellationPolicy;
import vn.com.pn.screen.m010HostCancallationPolicy.repository.HostCancellationPolicyRepository;
import vn.com.pn.screen.m006HostCategory.repository.HostCategoryRepository;
import vn.com.pn.screen.m004HostCity.repository.HostCityRepository;
import vn.com.pn.screen.m002Host.repository.HostDiscountRepository;
import vn.com.pn.screen.m007HostRoomType.repository.HostRoomTypeRepository;
import vn.com.pn.screen.m011HostRule.entity.Rule;
import vn.com.pn.screen.m012Language.entity.Language;
import vn.com.pn.screen.m012Language.repository.LanguageRepository;
import vn.com.pn.screen.m009HostProcedureCheckIn.repository.ProcedureCheckInRepository;
import vn.com.pn.screen.m011HostRule.repository.RuleRepository;
import vn.com.pn.screen.m001User.repository.UserRepository;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m002Host.entity.Host;
import vn.com.pn.screen.m004HostCity.entity.HostCity;
import vn.com.pn.screen.m006HostCategory.entity.HostCategory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class HostServiceImpl implements HostService {
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private HostCategoryRepository hostCategoryRepository;

    @Autowired
    private HostRoomTypeRepository hostRoomTypeRepository;

    @Autowired
    private HostCityRepository hostCityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HostCancellationPolicyRepository hostCancellationPolicyRepository;

    @Autowired
    private ProcedureCheckInRepository procedureCheckInRepository;

    @Autowired
    private CurrencyUnitRepository currencyUnitRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private HostDiscountRepository hostDiscountRepository;

    @Autowired
    private HostRepositoryCustom hostRepositoryCustom;

    @Autowired
    private ScheduledConfig scheduledConfig;

    @Override
    public BaseOutput getAll(Integer pageNo, Integer pageSize, String sortBy) {
        logger.info("HostServiceImpl.getAll");
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Host> pagedResult = hostRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return CommonFunction.successOutput(pagedResult.getContent(), pagedResult.getSize());
        } else {
            return CommonFunction.successOutput(new ArrayList<Host>());
        }
    }

    @Override
    public BaseOutput getByCityId(String cityId, Integer pageNo, Integer pageSize, String sortBy) {
        logger.info("HostServiceImpl.getByCityId");
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Host> pagedResult = hostRepository.findByHostCityId(Long.parseLong(cityId), paging);

        if (pagedResult.hasContent()) {
            return CommonFunction.successOutput(pagedResult.getContent(), pagedResult.getSize());
        } else {
            return CommonFunction.successOutput(new ArrayList<Host>());
        }
    }

    @Override
    public BaseOutput getId(String hostId) {
        logger.info("HostServiceImpl.getId");
        Host host = hostRepository.findById(Long.parseLong(hostId)).orElse(null);
        if (host != null) {
            return CommonFunction.successOutput(host);
        }
        throw new ResourceNotFoundException("Host", "id", hostId);
    }

    @Override
    public List<Host> search(String searchText, int pageNo) {
        final int HOST_PER_PAGE = 20;
        logger.info("HostServiceImpl.search");
        return hostRepositoryCustom.search(searchText, pageNo, HOST_PER_PAGE);
    }

    @Override
    public BaseOutput approve(String id) {
        logger.info("HostServiceImpl.approve");
        try {
            Host host = hostRepository.findById(
                    Long.parseLong(id)).orElse(null);
            if (host != null) {
                host.setApproved(true);
                return CommonFunction.successOutput(hostRepository.save(host));
            }
            return CommonFunction.failureOutput();

        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @Override
    public BaseOutput insert(HostDTO hostDTO, User userAgent) {
        logger.info("HostServiceImpl.insert");
        try {
            if (userAgent.getPhone() != null || !userAgent.getPhone().equalsIgnoreCase("")){
                Host host = getInsertHostInfo(hostDTO);
                host.setUser(userAgent);
                return CommonFunction.successOutput(hostRepository.save(host));
            }
            return CommonFunction.errorLogic(400, "Xin vui lòng cập nhật số điện thoại");
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    public BaseOutput discountHostPrice(HostDiscountDTO hostDiscountDTO) {
        logger.info("HostServiceImpl.discountHostPrice");
        try {
            LocalDate startDate = LocalDate.parse(hostDiscountDTO.getStartDiscountDay());
            LocalDate currentDate = LocalDate.now();

            if (startDate.isAfter(currentDate)) {

                HostDiscount hostDiscount = new HostDiscount();
                Host host = hostRepository.findById(Long.parseLong(hostDiscountDTO.getHostId())).orElseThrow(()
                        -> new ResourceNotFoundException("host", "id", hostDiscountDTO.getHostId()));

                java.time.LocalDateTime localStartDay = java.time.LocalDate.parse(hostDiscountDTO.getStartDiscountDay())
                        .atStartOfDay();
                java.time.LocalDateTime localEndDay = java.time.LocalDate.parse(hostDiscountDTO.getEndDiscountDay())
                        .atStartOfDay();

                hostDiscount.setPriceBeforeDiscountMondayToThursday(host.getStandardPriceMondayToThursday());
                hostDiscount.setPriceBeforeDiscountFridayToSunday(host.getStandardPriceFridayToSunday());
                hostDiscount.setDiscountPercent(Short.parseShort(hostDiscountDTO.getDiscountPercent()));
                hostDiscount.setStartDiscountDay(localStartDay);
                hostDiscount.setEndDiscountDay(localEndDay);
                hostDiscount.setHost(host);
                hostDiscountRepository.save(hostDiscount);

                setCountDownHandleDiscount(localStartDay, hostDiscountDTO, host);
                setCountdownPreviousPrice(localEndDay, host, hostDiscount);
            }
            return CommonFunction.errorLogic(400, "Ngày bắt đầu phải lớn hơn ngày hiện tại");
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    private void setCountDownHandleDiscount (LocalDateTime startDateTime, HostDiscountDTO hostDiscountDTO, Host host) {
        Runnable runnable = () -> handleDiscountPrice(hostDiscountDTO, host);
        ScheduledTaskRegistrar setUpCronTask = CommonFunction.setUpCronTask(startDateTime,runnable);
        scheduledConfig.configureTasks(setUpCronTask);
    }

    private void setCountdownPreviousPrice(LocalDateTime endDateTime, Host host, HostDiscount hostDiscount){
        Runnable runnable = () -> handleBackToPreviousPrice(hostDiscount, host);
        ScheduledTaskRegistrar setUpCronTask = CommonFunction.setUpCronTask(endDateTime,runnable);
        scheduledConfig.configureTasks(setUpCronTask);
    }

    private void handleBackToPreviousPrice (HostDiscount hostDiscount, Host host) {
        host.setStandardPriceMondayToThursday(hostDiscount.getPriceBeforeDiscountMondayToThursday());
        host.setStandardPriceFridayToSunday(hostDiscount.getPriceBeforeDiscountFridayToSunday());
        hostRepository.save(host);
    }

    private void handleDiscountPrice(HostDiscountDTO hostDiscountDTO, Host host) {
        float discountPercent = Float.parseFloat(hostDiscountDTO.getDiscountPercent()) / 100f;
        long discountPriceMondayToThursday = (long) (host.getStandardPriceMondayToThursday()
                * discountPercent);
        long discountPriceFridayToSunday = (long) (host.getStandardPriceFridayToSunday()
                * discountPercent);
        host.setStandardPriceMondayToThursday(discountPriceMondayToThursday);
        host.setStandardPriceFridayToSunday(discountPriceFridayToSunday);
        hostRepository.save(host);
    }

    @Override
    public BaseOutput update(HostUpdateDTO hostUpdateDTO) {
        logger.info("HostServiceImpl.update");
        try {
            Host host = getUpdateHostInfo(hostUpdateDTO);
            return CommonFunction.successOutput(host);
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    private Host getUpdateHostInfo(HostUpdateDTO hostUpdateDTO) {
        Host host = hostRepository.findById(Long.parseLong(hostUpdateDTO.getId())).orElseThrow(
                () -> new ResourceNotFoundException("Host Category", "id", hostUpdateDTO.getId()));
        if (hostUpdateDTO.getName() != null && hostUpdateDTO.getName() != "") {
            host.setName(hostUpdateDTO.getName());
        }
        if (hostUpdateDTO.getDescription() != null && hostUpdateDTO.getDescription() != "") {
            host.setDescription(hostUpdateDTO.getDescription());
        }
        if (hostUpdateDTO.getHostAgentId() != null && hostUpdateDTO.getHostAgentId() != "") {
            User user = userRepository.findById(
                    Long.parseLong(hostUpdateDTO.getHostAgentId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostAgent", "id", hostUpdateDTO.getHostAgentId()));
            host.setUser(user);
        }
        if (hostUpdateDTO.getHostCategoryId() != null && hostUpdateDTO.getHostCategoryId() != "") {
            HostCategory hostCategory = hostCategoryRepository.findById(
                    Long.parseLong(hostUpdateDTO.getHostCategoryId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostCategory", "id", hostUpdateDTO.getHostCategoryId()));
            host.setHostCategory(hostCategory);
        }
        if (hostUpdateDTO.getHostRoomTypeId() != null && hostUpdateDTO.getHostRoomTypeId() != "") {
            HostRoomType hostRoomType = hostRoomTypeRepository.findById(
                    Long.parseLong(hostUpdateDTO.getHostRoomTypeId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostRoomType", "id", hostUpdateDTO.getHostRoomTypeId()));
            host.setHostRoomType(hostRoomType);
        }
        if (hostUpdateDTO.getHostCityId() != null && hostUpdateDTO.getHostCityId() != "") {
            HostCity hostCity = hostCityRepository.findById(
                    Long.parseLong(hostUpdateDTO.getHostCityId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostCity", "id", hostUpdateDTO.getHostCityId()));
            host.setHostCity(hostCity);
        }
        if (hostUpdateDTO.getHostCancellationPolicyId() != null && hostUpdateDTO.getHostCancellationPolicyId() != "") {
            HostCancellationPolicy hostCancellationPolicy = hostCancellationPolicyRepository.findById(
                    Long.parseLong(hostUpdateDTO.getHostCancellationPolicyId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostCancellationPolicy", "id", hostUpdateDTO.getHostCancellationPolicyId()));
            host.setHostCancellationPolicy(hostCancellationPolicy);
        }

        if (hostUpdateDTO.getProcedureCheckInId() != null && hostUpdateDTO.getProcedureCheckInId() != "") {
            ProcedureCheckIn procedureCheckIn = procedureCheckInRepository.findById(
                    Long.parseLong(hostUpdateDTO.getProcedureCheckInId())).orElseThrow(()
                    -> new ResourceNotFoundException("ProcedureCheckIn", "id", hostUpdateDTO.getProcedureCheckInId()));
            host.setProcedureCheckIn(procedureCheckIn);
        }
        if (hostUpdateDTO.getCurrencyUnitId() != null && hostUpdateDTO.getCurrencyUnitId() != "") {
            CurrencyUnit currencyUnit = currencyUnitRepository.findById(
                    Long.parseLong(hostUpdateDTO.getCurrencyUnitId())).orElseThrow(()
                    -> new ResourceNotFoundException("CurrencyUnit", "id", hostUpdateDTO.getCurrencyUnitId()));
            host.setCurrencyUnit(currencyUnit);
        }
        if (hostUpdateDTO.getCurrencyUnitId() != null && hostUpdateDTO.getCurrencyUnitId() != "") {
            CurrencyUnit currencyUnit = currencyUnitRepository.findById(
                    Long.parseLong(hostUpdateDTO.getCurrencyUnitId())).orElseThrow(()
                    -> new ResourceNotFoundException("CurrencyUnit", "id", hostUpdateDTO.getCurrencyUnitId()));
            host.setCurrencyUnit(currencyUnit);
        }
        if (hostUpdateDTO.getRuleIds() != null && !hostUpdateDTO.getRuleIds().isEmpty()) {
            Set<String> ruleIdSet = hostUpdateDTO.getRuleIds();
            Set<Rule> rules = new HashSet<>();
            for (String ruleId : ruleIdSet) {
                Rule rule = ruleRepository.findById(
                        Long.parseLong(ruleId)).orElseThrow(()
                        -> new ResourceNotFoundException("Rule", "id", ruleId));
                rules.add(rule);
            }
            host.setRules(rules);
        }
        if (hostUpdateDTO.getLanguageIds() != null && !hostUpdateDTO.getLanguageIds().isEmpty()) {
            Set<String> languageIdSet = hostUpdateDTO.getLanguageIds();
            Set<Language> languages = new HashSet<>();
            for (String languageId : languageIdSet) {
                Language language = languageRepository.findById(
                        Long.parseLong(languageId)).orElseThrow(()
                        -> new ResourceNotFoundException("Language", "id", languageId));
                languages.add(language);
            }
            host.setLanguages(languages);
        }
        if (hostUpdateDTO.getAddress() != null && hostUpdateDTO.getAddress() != "") {
            host.setAddress(hostUpdateDTO.getAddress());
        }
        if (hostUpdateDTO.getLatitude() != null && hostUpdateDTO.getLatitude() != ""){
            host.setLatitude(hostUpdateDTO.getLatitude());
        }
        if (hostUpdateDTO.getLongitude() != null && hostUpdateDTO.getLongitude() != ""){
            host.setLongitude(hostUpdateDTO.getLongitude());
        }

        if (hostUpdateDTO.getBedroomCount() != null && hostUpdateDTO.getBedroomCount() != "") {
                host.setBedroomCount(Integer.parseInt(hostUpdateDTO.getBedroomCount()));
        }
        if (hostUpdateDTO.getBed() != null && hostUpdateDTO.getBed() != "") {
            host.setBed(Integer.parseInt(hostUpdateDTO.getBed()));
        }
        if (hostUpdateDTO.getBathroomCount() != null && hostUpdateDTO.getBathroomCount() != "") {
            host.setBathroomCount(Integer.parseInt(hostUpdateDTO.getBathroomCount()));
        }
        if (hostUpdateDTO.getStandardPriceMondayToThursday() != null && hostUpdateDTO.getStandardPriceMondayToThursday() != "") {
            host.setStandardPriceMondayToThursday(Long.parseLong(hostUpdateDTO.getStandardPriceMondayToThursday()));
        }
        if (hostUpdateDTO.getStandardPriceFridayToSunday() != null && hostUpdateDTO.getStandardPriceFridayToSunday() != "") {
            host.setStandardPriceFridayToSunday(Long.parseLong(hostUpdateDTO.getStandardPriceFridayToSunday()));
        }
        if (hostUpdateDTO.getCleaningCosts() != null && hostUpdateDTO.getCleaningCosts() != "") {
            host.setCleaningCosts(Long.parseLong(hostUpdateDTO.getCleaningCosts()));
        }
        if (hostUpdateDTO.getAdultCostsIncrease() != null && hostUpdateDTO.getAdultCostsIncrease() != "") {
            host.setAdultCostsIncrease(Long.parseLong(hostUpdateDTO.getAdultCostsIncrease()));
        }
        if (hostUpdateDTO.getChildrenCostsIncrease() != null && hostUpdateDTO.getChildrenCostsIncrease() != "") {
            host.setChildrenCostsIncrease(Long.parseLong(hostUpdateDTO.getChildrenCostsIncrease()));
        }
        if (hostUpdateDTO.getServiceChargePercent() != null && hostUpdateDTO.getServiceChargePercent() != "") {
            host.setServiceChargePercent(Byte.parseByte(hostUpdateDTO.getServiceChargePercent()));
        }
        if (hostUpdateDTO.getWeeklyDiscount() != null && hostUpdateDTO.getWeeklyDiscount() != "") {
            host.setWeeklyDiscount(Short.parseShort(hostUpdateDTO.getWeeklyDiscount()));
        }
        if (hostUpdateDTO.getMonthlyDiscount() != null && hostUpdateDTO.getMonthlyDiscount() != "") {
            host.setMonthlyDiscount(Short.parseShort(hostUpdateDTO.getMonthlyDiscount()));
        }
        if (hostUpdateDTO.getEarlyBirdDiscount() != null && hostUpdateDTO.getEarlyBirdDiscount() != "") {
            host.setEarlyBirdDiscount(Short.parseShort(hostUpdateDTO.getEarlyBirdDiscount()));
        }
        if (hostUpdateDTO.getDaysPriorToBooking() != null && hostUpdateDTO.getDaysPriorToBooking() != "") {
            host.setDaysPriorToBooking(Short.parseShort(hostUpdateDTO.getDaysPriorToBooking()));
        }
        if (hostUpdateDTO.getDaysPriorToBooking() != null && hostUpdateDTO.getDaysPriorToBooking() != "") {
            host.setDaysPriorToBooking(Short.parseShort(hostUpdateDTO.getDaysPriorToBooking()));
        }
        if (hostUpdateDTO.getLastMinuteDiscount() != null && hostUpdateDTO.getLastMinuteDiscount() != "") {
            host.setLastMinuteDiscount(Short.parseShort(hostUpdateDTO.getLastMinuteDiscount()));
        }
        if (hostUpdateDTO.getNumberOfStandardGuest() != null && hostUpdateDTO.getNumberOfStandardGuest() != "") {
            host.setNumberOfStandardGuest(Integer.parseInt(hostUpdateDTO.getNumberOfStandardGuest()));
        }
        if (hostUpdateDTO.getNumberOfMaximumGuest() != null && hostUpdateDTO.getNumberOfMaximumGuest() != "") {
            host.setNumberOfMaximumGuest(Integer.parseInt(hostUpdateDTO.getNumberOfMaximumGuest()));
        }
        if (hostUpdateDTO.getNumberOfAdultGuest() != null && hostUpdateDTO.getNumberOfAdultGuest() != "") {
            host.setNumberOfAdultGuest(Integer.parseInt(hostUpdateDTO.getNumberOfAdultGuest()));
        }
        if (hostUpdateDTO.getNumberOfChildrenGuest() != null && hostUpdateDTO.getNumberOfChildrenGuest() != "") {
            host.setNumberOfChildrenGuest(Integer.parseInt(hostUpdateDTO.getNumberOfChildrenGuest()));
        }
        if (hostUpdateDTO.getNumberOfInfantGuest() != null && hostUpdateDTO.getNumberOfInfantGuest() != "") {
            host.setNumberOfInfantGuest(Integer.parseInt(hostUpdateDTO.getNumberOfInfantGuest()));
        }
        if (hostUpdateDTO.getIsAddChildrenAndInfantIntoMaximumGuest() != null
                && hostUpdateDTO.getIsAddChildrenAndInfantIntoMaximumGuest() != "") {
            if (hostUpdateDTO.getIsAddChildrenAndInfantIntoMaximumGuest().equals("0")) {
                host.setAddChildrenAndInfantIntoMaximumGuest(false);
            }
            if (hostUpdateDTO.getIsAddChildrenAndInfantIntoMaximumGuest().equals("1")) {
                host.setAddChildrenAndInfantIntoMaximumGuest(true);
            }
        }
        if (hostUpdateDTO.getNumberOfMinimumNight() != null && hostUpdateDTO.getNumberOfMinimumNight() != "") {
            host.setNumberOfMinimumNight(Integer.parseInt(hostUpdateDTO.getNumberOfMinimumNight()));
        }
        if (hostUpdateDTO.getNumberOfMaximumNight() != null && hostUpdateDTO.getNumberOfMaximumNight() != "") {
            host.setNumberOfMaximumNight(Integer.parseInt(hostUpdateDTO.getNumberOfMaximumNight()));
        }
        if (hostUpdateDTO.getAcreage() != null && hostUpdateDTO.getAcreage() != "") {
            host.setAcreage(Integer.parseInt(hostUpdateDTO.getAcreage()));
        }
        if (hostUpdateDTO.getEarliestCheckIn() != null && hostUpdateDTO.getEarliestCheckIn() != "") {
            host.setEarliestCheckIn(LocalTime.parse(hostUpdateDTO.getEarliestCheckIn()));
        }
        if (hostUpdateDTO.getLatestCheckIn() != null && hostUpdateDTO.getLatestCheckIn() != "") {
            host.setLatestCheckIn(LocalTime.parse(hostUpdateDTO.getLatestCheckIn()));
        }
        if (hostUpdateDTO.getCheckOutTime() != null && hostUpdateDTO.getCheckOutTime() != "") {
            host.setCheckOutTime(LocalTime.parse(hostUpdateDTO.getCheckOutTime()));
        }
        if (hostUpdateDTO.getJapaneseCushion() != null && hostUpdateDTO.getJapaneseCushion() != "") {
            host.setJapaneseCushion(Integer.parseInt(hostUpdateDTO.getJapaneseCushion()));
        }
        if (hostUpdateDTO.getSofa() != null && hostUpdateDTO.getSofa() != "") {
            host.setSofa(Integer.parseInt(hostUpdateDTO.getSofa()));
        }
        if (hostUpdateDTO.getBunk() != null && hostUpdateDTO.getBunk() != "") {
            host.setBunk(Integer.parseInt(hostUpdateDTO.getBunk()));
        }
        if (hostUpdateDTO.getKitchenCount() != null && hostUpdateDTO.getKitchenCount() != "") {
            host.setKitchenCount(Integer.parseInt(hostUpdateDTO.getKitchenCount()));
        }
        if (hostUpdateDTO.getCheckInInstructions() != null && hostUpdateDTO.getCheckInInstructions() != "") {
            host.setCheckInInstructions(hostUpdateDTO.getCheckInInstructions());
        }
        if (hostUpdateDTO.getUsingConvenientInstructions() != null && hostUpdateDTO.getUsingConvenientInstructions() != "") {
            host.setUsingConvenientInstructions(hostUpdateDTO.getUsingConvenientInstructions());
        }
        if (hostUpdateDTO.getIsHasGardenView() != null && hostUpdateDTO.getIsHasGardenView() != "") {
            if (hostUpdateDTO.getIsHasGardenView().equals("0")) {
                host.setHasGardenView(false);
            }
            if (hostUpdateDTO.getIsHasGardenView().equals("1")) {
                host.setHasGardenView(true);
            }
        }
        if (hostUpdateDTO.getIsHasParkView() != null && hostUpdateDTO.getIsHasParkView() != "") {
            if (hostUpdateDTO.getIsHasParkView().equals("0")) {
                host.setHasParkView(false);
            }
            if (hostUpdateDTO.getIsHasParkView().equals("1")) {
                host.setHasParkView(true);
            }
        }
        if (hostUpdateDTO.getIsHasParkView() != null && hostUpdateDTO.getIsHasParkView() != "") {
            if (hostUpdateDTO.getIsHasParkView().equals("0")) {
                host.setHasParkView(false);
            }
            if (hostUpdateDTO.getIsHasParkView().equals("1")) {
                host.setHasParkView(true);
            }
        }
        if (hostUpdateDTO.getIsHasForestView() != null && hostUpdateDTO.getIsHasForestView() != "") {
            if (hostUpdateDTO.getIsHasForestView().equals("0")) {
                host.setHasForestView(false);
            }
            if (hostUpdateDTO.getIsHasForestView().equals("1")) {
                host.setHasForestView(true);
            }
        }
        if (hostUpdateDTO.getIsHasElevator() != null && hostUpdateDTO.getIsHasElevator() != "") {
            if (hostUpdateDTO.getIsHasElevator().equals("0")) {
                host.setHasElevator(false);
            }
            if (hostUpdateDTO.getIsHasElevator().equals("1")) {
                host.setHasElevator(true);
            }
        }
        if (hostUpdateDTO.getIsHasStair() != null && hostUpdateDTO.getIsHasStair() != "") {
            if (hostUpdateDTO.getIsHasStair().equals("0")) {
                host.setHasStair(false);
            }
            if (hostUpdateDTO.getIsHasStair().equals("1")) {
                host.setHasStair(true);
            }
        }
        if (hostUpdateDTO.getIsHasMeeting() != null && hostUpdateDTO.getIsHasMeeting() != "") {
            if (hostUpdateDTO.getIsHasMeeting().equals("0")) {
                host.setHasMeeting(false);
            }
            if (hostUpdateDTO.getIsHasMeeting().equals("1")) {
                host.setHasMeeting(true);
            }
        }
        if (hostUpdateDTO.getIsHasGarbageCollectionArea() != null && hostUpdateDTO.getIsHasGarbageCollectionArea() != "") {
            if (hostUpdateDTO.getIsHasGarbageCollectionArea().equals("0")) {
                host.setHasGarbageCollectionArea(false);
            }
            if (hostUpdateDTO.getIsHasGarbageCollectionArea().equals("1")) {
                host.setHasGarbageCollectionArea(true);
            }
        }
        if (hostUpdateDTO.getIsHas24HourReception() != null && hostUpdateDTO.getIsHas24HourReception() != "") {
            if (hostUpdateDTO.getIsHas24HourReception().equals("0")) {
                host.setHas24HourReception(false);
            }
            if (hostUpdateDTO.getIsHas24HourReception().equals("1")) {
                host.setHas24HourReception(true);
            }
        }
        if (hostUpdateDTO.getIsHasSmokingArea() != null && hostUpdateDTO.getIsHasSmokingArea() != "") {
            if (hostUpdateDTO.getIsHasSmokingArea().equals("0")) {
                host.setHasSmokingArea(false);
            }
            if (hostUpdateDTO.getIsHasSmokingArea().equals("1")) {
                host.setHasSmokingArea(true);
            }
        }
        if (hostUpdateDTO.getIsHasOutdoorParkingLot() != null && hostUpdateDTO.getIsHasOutdoorParkingLot() != "") {
            if (hostUpdateDTO.getIsHasOutdoorParkingLot().equals("0")) {
                host.setHasOutdoorParkingLot(false);
            }
            if (hostUpdateDTO.getIsHasOutdoorParkingLot().equals("1")) {
                host.setHasOutdoorParkingLot(true);
            }
        }
        if (hostUpdateDTO.getIsHasIndoorParkingLot() != null && hostUpdateDTO.getIsHasIndoorParkingLot() != "") {
            if (hostUpdateDTO.getIsHasIndoorParkingLot().equals("0")) {
                host.setHasIndoorParkingLot(false);
            }
            if (hostUpdateDTO.getIsHasIndoorParkingLot().equals("1")) {
                host.setHasIndoorParkingLot(true);
            }
        }
        if (hostUpdateDTO.getIsHasSharedCarPark() != null && hostUpdateDTO.getIsHasSharedCarPark() != "") {
            if (hostUpdateDTO.getIsHasSharedCarPark().equals("0")) {
                host.setHasSharedCarPark(false);
            }
            if (hostUpdateDTO.getIsHasSharedCarPark().equals("1")) {
                host.setHasSharedCarPark(true);
            }
        }
        if (hostUpdateDTO.getIsHasCarPark() != null && hostUpdateDTO.getIsHasCarPark() != "") {
            if (hostUpdateDTO.getIsHasCarPark().equals("0")) {
                host.setHasCarPark(false);
            }
            if (hostUpdateDTO.getIsHasCarPark().equals("1")) {
                host.setHasCarPark(true);
            }
        }
        if (hostUpdateDTO.getIsHasBilliard() != null && hostUpdateDTO.getIsHasBilliard() != "") {
            if (hostUpdateDTO.getIsHasBilliard().equals("0")) {
                host.setHasBilliard(false);
            }
            if (hostUpdateDTO.getIsHasBilliard().equals("1")) {
                host.setHasBilliard(true);
            }
        }
        if (hostUpdateDTO.getIsHasPingPong() != null && hostUpdateDTO.getIsHasPingPong() != "") {
            if (hostUpdateDTO.getIsHasPingPong().equals("0")) {
                host.setHasPingPong(false);
            }
            if (hostUpdateDTO.getIsHasPingPong().equals("1")) {
                host.setHasPingPong(true);
            }
        }
        if (hostUpdateDTO.getIsHasFishing() != null && hostUpdateDTO.getIsHasFishing() != "") {
            if (hostUpdateDTO.getIsHasFishing().equals("0")) {
                host.setHasFishing(false);
            }
            if (hostUpdateDTO.getIsHasFishing().equals("1")) {
                host.setHasFishing(true);
            }
        }
        if (hostUpdateDTO.getIsHasTennisCourse() != null && hostUpdateDTO.getIsHasTennisCourse() != "") {
            if (hostUpdateDTO.getIsHasTennisCourse().equals("0")) {
                host.setHasTennisCourse(false);
            }
            if (hostUpdateDTO.getIsHasTennisCourse().equals("1")) {
                host.setHasTennisCourse(true);
            }
        }
        if (hostUpdateDTO.getIsHasTerrace() != null && hostUpdateDTO.getIsHasTerrace() != "") {
            if (hostUpdateDTO.getIsHasTerrace().equals("0")) {
                host.setHasTerrace(false);
            }
            if (hostUpdateDTO.getIsHasTerrace().equals("1")) {
                host.setHasTerrace(true);
            }
        }
        if (hostUpdateDTO.getIsHasBBQAreaWithDiningTable() != null && hostUpdateDTO.getIsHasBBQAreaWithDiningTable() != "") {
            if (hostUpdateDTO.getIsHasBBQAreaWithDiningTable().equals("0")) {
                host.setHasBBQAreaWithDiningTable(false);
            }
            if (hostUpdateDTO.getIsHasBBQAreaWithDiningTable().equals("1")) {
                host.setHasBBQAreaWithDiningTable(true);
            }
        }
        if (hostUpdateDTO.getIsHasChildrenPlayArea() != null && hostUpdateDTO.getIsHasChildrenPlayArea() != "") {
            if (hostUpdateDTO.getIsHasChildrenPlayArea().equals("0")) {
                host.setHasChildrenPlayArea(false);
            }
            if (hostUpdateDTO.getIsHasChildrenPlayArea().equals("1")) {
                host.setHasChildrenPlayArea(true);
            }
        }
        if (hostUpdateDTO.getIsHasGarden() != null && hostUpdateDTO.getIsHasGarden() != "") {
            if (hostUpdateDTO.getIsHasGarden().equals("0")) {
                host.setHasGarden(false);
            }
            if (hostUpdateDTO.getIsHasGarden().equals("1")) {
                host.setHasGarden(true);
            }
        }
        if (hostUpdateDTO.getIsHasInfinityPool() != null && hostUpdateDTO.getIsHasInfinityPool() != "") {
            if (hostUpdateDTO.getIsHasInfinityPool().equals("0")) {
                host.setHasInfinityPool(false);
            }
            if (hostUpdateDTO.getIsHasInfinityPool().equals("1")) {
                host.setHasInfinityPool(true);
            }
        }
        if (hostUpdateDTO.getIsHasOutdoorPool() != null && hostUpdateDTO.getIsHasOutdoorPool() != "") {
            if (hostUpdateDTO.getIsHasOutdoorPool().equals("0")) {
                host.setHasOutdoorPool(false);
            }
            if (hostUpdateDTO.getIsHasOutdoorPool().equals("1")) {
                host.setHasOutdoorPool(true);
            }
        }
        if (hostUpdateDTO.getIsHasIndoorSwimmingPool() != null && hostUpdateDTO.getIsHasIndoorSwimmingPool() != "") {
            if (hostUpdateDTO.getIsHasIndoorSwimmingPool().equals("0")) {
                host.setHasIndoorSwimmingPool(false);
            }
            if (hostUpdateDTO.getIsHasIndoorSwimmingPool().equals("1")) {
                host.setHasIndoorSwimmingPool(true);
            }
        }
        if (hostUpdateDTO.getIsHasGym() != null && hostUpdateDTO.getIsHasGym() != "") {
            if (hostUpdateDTO.getIsHasGym().equals("0")) {
                host.setHasGym(false);
            }
            if (hostUpdateDTO.getIsHasGym().equals("1")) {
                host.setHasGym(true);
            }
        }
        if (hostUpdateDTO.getIsHasYoga() != null && hostUpdateDTO.getIsHasYoga() != "") {
            if (hostUpdateDTO.getIsHasYoga().equals("0")) {
                host.setHasYoga(false);
            }
            if (hostUpdateDTO.getIsHasYoga().equals("1")) {
                host.setHasYoga(true);
            }
        }
        if (hostUpdateDTO.getIsHasSauna() != null && hostUpdateDTO.getIsHasSauna() != "") {
            if (hostUpdateDTO.getIsHasSauna().equals("0")) {
                host.setHasSauna(false);
            }
            if (hostUpdateDTO.getIsHasSauna().equals("1")) {
                host.setHasSauna(true);
            }
        }
        if (hostUpdateDTO.getIsHasSpa() != null && hostUpdateDTO.getIsHasSpa() != "") {
            if (hostUpdateDTO.getIsHasSpa().equals("0")) {
                host.setHasSpa(false);
            }
            if (hostUpdateDTO.getIsHasSpa().equals("1")) {
                host.setHasSpa(true);
            }
        }
        if (hostUpdateDTO.getIsHasHairdressers() != null && hostUpdateDTO.getIsHasHairdressers() != "") {
            if (hostUpdateDTO.getIsHasHairdressers().equals("0")) {
                host.setHasHairdressers(false);
            }
            if (hostUpdateDTO.getIsHasHairdressers().equals("1")) {
                host.setHasHairdressers(true);
            }
        }
        if (hostUpdateDTO.getIsHasClinic() != null && hostUpdateDTO.getIsHasClinic() != "") {
            if (hostUpdateDTO.getIsHasClinic().equals("0")) {
                host.setHasClinic(false);
            }
            if (hostUpdateDTO.getIsHasClinic().equals("1")) {
                host.setHasClinic(true);
            }
        }
        if (hostUpdateDTO.getIsHasPharmacies() != null && hostUpdateDTO.getIsHasPharmacies() != "") {
            if (hostUpdateDTO.getIsHasPharmacies().equals("0")) {
                host.setHasPharmacies(false);
            }
            if (hostUpdateDTO.getIsHasPharmacies().equals("1")) {
                host.setHasPharmacies(true);
            }
        }
        if (hostUpdateDTO.getIsHasCoffeeShop() != null && hostUpdateDTO.getIsHasCoffeeShop() != "") {
            if (hostUpdateDTO.getIsHasCoffeeShop().equals("0")) {
                host.setHasCoffeeShop(false);
            }
            if (hostUpdateDTO.getIsHasCoffeeShop().equals("1")) {
                host.setHasCoffeeShop(true);
            }
        }
        if (hostUpdateDTO.getIsHasBar() != null && hostUpdateDTO.getIsHasBar() != "") {
            if (hostUpdateDTO.getIsHasBar().equals("0")) {
                host.setHasBar(false);
            }
            if (hostUpdateDTO.getIsHasBar().equals("1")) {
                host.setHasBar(true);
            }
        }
        if (hostUpdateDTO.getIsHasRestaurant() != null && hostUpdateDTO.getIsHasRestaurant() != "") {
            if (hostUpdateDTO.getIsHasRestaurant().equals("0")) {
                host.setHasRestaurant(false);
            }
            if (hostUpdateDTO.getIsHasRestaurant().equals("1")) {
                host.setHasRestaurant(true);
            }
        }
        if (hostUpdateDTO.getIsHasShoppingMall() != null && hostUpdateDTO.getIsHasShoppingMall() != "") {
            if (hostUpdateDTO.getIsHasShoppingMall().equals("0")) {
                host.setHasShoppingMall(false);
            }
            if (hostUpdateDTO.getIsHasShoppingMall().equals("1")) {
                host.setHasShoppingMall(true);
            }
        }
        if (hostUpdateDTO.getIsHasSouvenirStore() != null && hostUpdateDTO.getIsHasSouvenirStore() != "") {
            if (hostUpdateDTO.getIsHasSouvenirStore().equals("0")) {
                host.setHasSouvenirStore(false);
            }
            if (hostUpdateDTO.getIsHasSouvenirStore().equals("1")) {
                host.setHasSouvenirStore(true);
            }
        }
        if (hostUpdateDTO.getIsNearTheGolfCourse() != null && hostUpdateDTO.getIsNearTheGolfCourse() != "") {
            if (hostUpdateDTO.getIsNearTheGolfCourse().equals("0")) {
                host.setNearTheGolfCourse(false);
            }
            if (hostUpdateDTO.getIsNearTheGolfCourse().equals("1")) {
                host.setNearTheGolfCourse(true);
            }
        }
        if (hostUpdateDTO.getIsCloseToTheBusStop() != null && hostUpdateDTO.getIsCloseToTheBusStop() != "") {
            if (hostUpdateDTO.getIsCloseToTheBusStop().equals("0")) {
                host.setCloseToTheBusStop(false);
            }
            if (hostUpdateDTO.getIsCloseToTheBusStop().equals("1")) {
                host.setCloseToTheBusStop(true);
            }
        }
        if (hostUpdateDTO.getIsNearTheMuseum() != null && hostUpdateDTO.getIsNearTheMuseum() != "") {
            if (hostUpdateDTO.getIsNearTheMuseum().equals("0")) {
                host.setNearTheMuseum(false);
            }
            if (hostUpdateDTO.getIsNearTheMuseum().equals("1")) {
                host.setNearTheMuseum(true);
            }
        }
        if (hostUpdateDTO.getIsNearTheATM() != null && hostUpdateDTO.getIsNearTheATM() != "") {
            if (hostUpdateDTO.getIsNearTheATM().equals("0")) {
                host.setNearTheATM(false);
            }
            if (hostUpdateDTO.getIsNearTheATM().equals("1")) {
                host.setNearTheATM(true);
            }
        }
        if (hostUpdateDTO.getIsNearThePark() != null && hostUpdateDTO.getIsNearThePark() != "") {
            if (hostUpdateDTO.getIsNearThePark().equals("0")) {
                host.setNearThePark(false);
            }
            if (hostUpdateDTO.getIsNearThePark().equals("1")) {
                host.setNearThePark(true);
            }
        }
        if (hostUpdateDTO.getIsFreeBreakfast() != null && hostUpdateDTO.getIsFreeBreakfast() != "") {
            if (hostUpdateDTO.getIsFreeBreakfast().equals("0")) {
                host.setFreeBreakfast(false);
            }
            if (hostUpdateDTO.getIsFreeBreakfast().equals("1")) {
                host.setFreeBreakfast(true);
            }
        }
        if (hostUpdateDTO.getIsAirportTransfer() != null && hostUpdateDTO.getIsAirportTransfer() != "") {
            if (hostUpdateDTO.getIsAirportTransfer().equals("0")) {
                host.setAirportTransfer(false);
            }
            if (hostUpdateDTO.getIsAirportTransfer().equals("1")) {
                host.setAirportTransfer(true);
            }
        }
        if (hostUpdateDTO.getIsHasVehicleRental() != null && hostUpdateDTO.getIsHasVehicleRental() != "") {
            if (hostUpdateDTO.getIsHasVehicleRental().equals("0")) {
                host.setHasVehicleRental(false);
            }
            if (hostUpdateDTO.getIsHasVehicleRental().equals("1")) {
                host.setHasVehicleRental(true);
            }
        }
        if (hostUpdateDTO.getIsHasFreeBicycles() != null && hostUpdateDTO.getIsHasFreeBicycles() != "") {
            if (hostUpdateDTO.getIsHasFreeBicycles().equals("0")) {
                host.setHasFreeBicycles(false);
            }
            if (hostUpdateDTO.getIsHasFreeBicycles().equals("1")) {
                host.setHasFreeBicycles(true);
            }
        }
        if (hostUpdateDTO.getIsHasTicketingService() != null && hostUpdateDTO.getIsHasTicketingService() != "") {
            if (hostUpdateDTO.getIsHasTicketingService().equals("0")) {
                host.setHasTicketingService(false);
            }
            if (hostUpdateDTO.getIsHasTicketingService().equals("1")) {
                host.setHasTicketingService(true);
            }
        }
        if (hostUpdateDTO.getIsHasLaundry() != null && hostUpdateDTO.getIsHasLaundry() != "") {
            if (hostUpdateDTO.getIsHasLaundry().equals("0")) {
                host.setHasLaundry(false);
            }
            if (hostUpdateDTO.getIsHasLaundry().equals("1")) {
                host.setHasLaundry(true);
            }
        }
        if (hostUpdateDTO.getIsHasSubmitItemsBeforeCheckIn() != null && hostUpdateDTO.getIsHasSubmitItemsBeforeCheckIn() != "") {
            if (hostUpdateDTO.getIsHasSubmitItemsBeforeCheckIn().equals("0")) {
                host.setHasSubmitItemsBeforeCheckIn(false);
            }
            if (hostUpdateDTO.getIsHasSubmitItemsBeforeCheckIn().equals("1")) {
                host.setHasSubmitItemsBeforeCheckIn(true);
            }
        }
        return host;
    }

    private Host getInsertHostInfo(HostDTO hostDTO) {
        logger.info("HostServiceImpl.getInsertHostInfo");
        Host host = new Host();
        if (hostDTO.getName() != null && hostDTO.getName() != "") {
            host.setName(hostDTO.getName());
        }
        if (hostDTO.getDescription() != null && hostDTO.getDescription() != "") {
            host.setDescription(hostDTO.getDescription());
        }
        if (hostDTO.getHostCategoryId() != null && hostDTO.getHostCategoryId() != "") {
            HostCategory hostCategory = hostCategoryRepository.findById(
                    Long.parseLong(hostDTO.getHostCategoryId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostCategory", "id", hostDTO.getHostCategoryId()));
            host.setHostCategory(hostCategory);
        }
        if (hostDTO.getHostRoomTypeId() != null && hostDTO.getHostRoomTypeId() != "") {
            HostRoomType hostRoomType = hostRoomTypeRepository.findById(
                    Long.parseLong(hostDTO.getHostRoomTypeId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostRoomType", "id", hostDTO.getHostRoomTypeId()));
            host.setHostRoomType(hostRoomType);
        }
        if (hostDTO.getHostCityId() != null && hostDTO.getHostCityId() != "") {
            HostCity hostCity = hostCityRepository.findById(
                    Long.parseLong(hostDTO.getHostCityId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostCity", "id", hostDTO.getHostCityId()));
            host.setHostCity(hostCity);
        }
        if (hostDTO.getHostCancellationPolicyId() != null && hostDTO.getHostCancellationPolicyId() != "") {
            HostCancellationPolicy hostCancellationPolicy = hostCancellationPolicyRepository.findById(
                    Long.parseLong(hostDTO.getHostCancellationPolicyId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostCancellationPolicy", "id", hostDTO.getHostCancellationPolicyId()));
            host.setHostCancellationPolicy(hostCancellationPolicy);
        }

        if (hostDTO.getProcedureCheckInId() != null && hostDTO.getProcedureCheckInId() != "") {
            ProcedureCheckIn procedureCheckIn = procedureCheckInRepository.findById(
                    Long.parseLong(hostDTO.getProcedureCheckInId())).orElseThrow(()
                    -> new ResourceNotFoundException("ProcedureCheckIn", "id", hostDTO.getProcedureCheckInId()));
            host.setProcedureCheckIn(procedureCheckIn);
        }
        if (hostDTO.getCurrencyUnitId() != null && hostDTO.getCurrencyUnitId() != "") {
            CurrencyUnit currencyUnit = currencyUnitRepository.findById(
                    Long.parseLong(hostDTO.getCurrencyUnitId())).orElseThrow(()
                    -> new ResourceNotFoundException("CurrencyUnit", "id", hostDTO.getCurrencyUnitId()));
            host.setCurrencyUnit(currencyUnit);
        }
        if (hostDTO.getCurrencyUnitId() != null && hostDTO.getCurrencyUnitId() != "") {
            CurrencyUnit currencyUnit = currencyUnitRepository.findById(
                    Long.parseLong(hostDTO.getCurrencyUnitId())).orElseThrow(()
                    -> new ResourceNotFoundException("CurrencyUnit", "id", hostDTO.getCurrencyUnitId()));
            host.setCurrencyUnit(currencyUnit);
        }
        if (hostDTO.getRuleIds() != null && !hostDTO.getRuleIds().isEmpty()) {
            Set<String> ruleIdSet = hostDTO.getRuleIds();
            Set<Rule> rules = new HashSet<>();
            for (String ruleId : ruleIdSet) {
                Rule rule = ruleRepository.findById(
                        Long.parseLong(ruleId)).orElseThrow(()
                        -> new ResourceNotFoundException("Rule", "id", ruleId));
                rules.add(rule);
            }
            host.setRules(rules);
        }
        if (hostDTO.getLanguageIds() != null && !hostDTO.getRuleIds().isEmpty()) {
            Set<String> languageIdSet = hostDTO.getLanguageIds();
            Set<Language> languages = new HashSet<>();
            for (String languageId : languageIdSet) {
                Language language = languageRepository.findById(
                        Long.parseLong(languageId)).orElseThrow(()
                        -> new ResourceNotFoundException("Language", "id", languageId));
                languages.add(language);
            }
            host.setLanguages(languages);
        }
        if (hostDTO.getAddress() != null && hostDTO.getAddress() != "") {
            host.setAddress(hostDTO.getAddress());
        }
        if (hostDTO.getLatitude() != null && hostDTO.getLatitude() != ""){
            host.setLatitude(hostDTO.getLatitude());
        }
        if (hostDTO.getLatitude() != null && hostDTO.getLatitude() != ""){
            host.setLongitude(hostDTO.getLongitude());
        }
        if (hostDTO.getBedroomCount() != null && hostDTO.getBedroomCount() != "") {
            host.setBedroomCount(Integer.parseInt(hostDTO.getBedroomCount()));
        }
        if (hostDTO.getBed() != null && hostDTO.getBed() != "") {
            host.setBed(Integer.parseInt(hostDTO.getBed()));
        }
        if (hostDTO.getBathroomCount() != null && hostDTO.getBathroomCount() != "") {
            host.setBathroomCount(Integer.parseInt(hostDTO.getBathroomCount()));
        }
        if (hostDTO.getStandardPriceMondayToThursday() != null && hostDTO.getStandardPriceMondayToThursday() != "") {
            host.setStandardPriceMondayToThursday(Long.parseLong(hostDTO.getStandardPriceMondayToThursday()));
        }
        if (hostDTO.getStandardPriceFridayToSunday() != null && hostDTO.getStandardPriceFridayToSunday() != "") {
            host.setStandardPriceFridayToSunday(Long.parseLong(hostDTO.getStandardPriceFridayToSunday()));
        }
        if (hostDTO.getCleaningCosts() != null && hostDTO.getCleaningCosts() != "") {
            host.setCleaningCosts(Long.parseLong(hostDTO.getCleaningCosts()));
        }
        if (hostDTO.getAdultCostsIncrease() != null && hostDTO.getAdultCostsIncrease() != "") {
            host.setAdultCostsIncrease(Long.parseLong(hostDTO.getAdultCostsIncrease()));
        }
        if (hostDTO.getChildrenCostsIncrease() != null && hostDTO.getChildrenCostsIncrease() != "") {
            host.setChildrenCostsIncrease(Long.parseLong(hostDTO.getChildrenCostsIncrease()));
        }
        if (hostDTO.getServiceChargePercent() != null && hostDTO.getServiceChargePercent() != "") {
            host.setServiceChargePercent(Byte.parseByte(hostDTO.getServiceChargePercent()));
        }
        if (hostDTO.getWeeklyDiscount() != null && hostDTO.getWeeklyDiscount() != "") {
            host.setWeeklyDiscount(Short.parseShort(hostDTO.getWeeklyDiscount()));
        }
        if (hostDTO.getMonthlyDiscount() != null && hostDTO.getMonthlyDiscount() != "") {
            host.setMonthlyDiscount(Short.parseShort(hostDTO.getMonthlyDiscount()));
        }
        if (hostDTO.getEarlyBirdDiscount() != null && hostDTO.getEarlyBirdDiscount() != "") {
            host.setEarlyBirdDiscount(Short.parseShort(hostDTO.getEarlyBirdDiscount()));
        }
        if (hostDTO.getDaysPriorToBooking() != null && hostDTO.getDaysPriorToBooking() != "") {
            host.setDaysPriorToBooking(Short.parseShort(hostDTO.getDaysPriorToBooking()));
        }
        if (hostDTO.getDaysPriorToBooking() != null && hostDTO.getDaysPriorToBooking() != "") {
            host.setDaysPriorToBooking(Short.parseShort(hostDTO.getDaysPriorToBooking()));
        }
        if (hostDTO.getLastMinuteDiscount() != null && hostDTO.getLastMinuteDiscount() != "") {
            host.setLastMinuteDiscount(Short.parseShort(hostDTO.getLastMinuteDiscount()));
        }
        if (hostDTO.getNumberOfStandardGuest() != null && hostDTO.getNumberOfStandardGuest() != "") {
            host.setNumberOfStandardGuest(Integer.parseInt(hostDTO.getNumberOfStandardGuest()));
        }
        if (hostDTO.getNumberOfMaximumGuest() != null && hostDTO.getNumberOfMaximumGuest() != "") {
            host.setNumberOfMaximumGuest(Integer.parseInt(hostDTO.getNumberOfMaximumGuest()));
        }
        if (hostDTO.getNumberOfAdultGuest() != null && hostDTO.getNumberOfAdultGuest() != "") {
            host.setNumberOfAdultGuest(Integer.parseInt(hostDTO.getNumberOfAdultGuest()));
        }
        if (hostDTO.getNumberOfChildrenGuest() != null && hostDTO.getNumberOfChildrenGuest() != "") {
            host.setNumberOfChildrenGuest(Integer.parseInt(hostDTO.getNumberOfChildrenGuest()));
        }
        if (hostDTO.getNumberOfInfantGuest() != null && hostDTO.getNumberOfInfantGuest() != "") {
            host.setNumberOfInfantGuest(Integer.parseInt(hostDTO.getNumberOfInfantGuest()));
        }
        if (hostDTO.getIsAddChildrenAndInfantIntoMaximumGuest() != null
                && hostDTO.getIsAddChildrenAndInfantIntoMaximumGuest() != "") {
            if (hostDTO.getIsAddChildrenAndInfantIntoMaximumGuest().equals("0")) {
                host.setAddChildrenAndInfantIntoMaximumGuest(false);
            }
            if (hostDTO.getIsAddChildrenAndInfantIntoMaximumGuest().equals("1")) {
                host.setAddChildrenAndInfantIntoMaximumGuest(true);
            }
        }
        if (hostDTO.getNumberOfMinimumNight() != null && hostDTO.getNumberOfMinimumNight() != "") {
            host.setNumberOfMinimumNight(Integer.parseInt(hostDTO.getNumberOfMinimumNight()));
        }
        if (hostDTO.getNumberOfMaximumNight() != null && hostDTO.getNumberOfMaximumNight() != "") {
            host.setNumberOfMaximumNight(Integer.parseInt(hostDTO.getNumberOfMaximumNight()));
        }
        if (hostDTO.getAcreage() != null && hostDTO.getAcreage() != "") {
            host.setAcreage(Integer.parseInt(hostDTO.getAcreage()));
        }
        if (hostDTO.getEarliestCheckIn() != null && hostDTO.getEarliestCheckIn() != "") {
            host.setEarliestCheckIn(LocalTime.parse(hostDTO.getEarliestCheckIn()));
        }
        if (hostDTO.getLatestCheckIn() != null && hostDTO.getLatestCheckIn() != "") {
            host.setLatestCheckIn(LocalTime.parse(hostDTO.getLatestCheckIn()));
        }
        if (hostDTO.getCheckOutTime() != null && hostDTO.getCheckOutTime() != "") {
            host.setCheckOutTime(LocalTime.parse(hostDTO.getCheckOutTime()));
        }
        if (hostDTO.getJapaneseCushion() != null && hostDTO.getJapaneseCushion() != "") {
            host.setJapaneseCushion(Integer.parseInt(hostDTO.getJapaneseCushion()));
        }
        if (hostDTO.getSofa() != null && hostDTO.getSofa() != "") {
            host.setSofa(Integer.parseInt(hostDTO.getSofa()));
        }
        if (hostDTO.getBunk() != null && hostDTO.getBunk() != "") {
            host.setBunk(Integer.parseInt(hostDTO.getBunk()));
        }
        if (hostDTO.getKitchenCount() != null && hostDTO.getKitchenCount() != "") {
            host.setKitchenCount(Integer.parseInt(hostDTO.getKitchenCount()));
        }
        if (hostDTO.getCheckInInstructions() != null && hostDTO.getCheckInInstructions() != "") {
            host.setCheckInInstructions(hostDTO.getCheckInInstructions());
        }
        if (hostDTO.getUsingConvenientInstructions() != null && hostDTO.getUsingConvenientInstructions() != "") {
            host.setUsingConvenientInstructions(hostDTO.getUsingConvenientInstructions());
        }
        if (hostDTO.getIsHasGardenView() != null && hostDTO.getIsHasGardenView() != "") {
            if (hostDTO.getIsHasGardenView().equals("0")) {
                host.setHasGardenView(false);
            }
            if (hostDTO.getIsHasGardenView().equals("1")) {
                host.setHasGardenView(true);
            }
        }
        if (hostDTO.getIsHasParkView() != null && hostDTO.getIsHasParkView() != "") {
            if (hostDTO.getIsHasParkView().equals("0")) {
                host.setHasParkView(false);
            }
            if (hostDTO.getIsHasParkView().equals("1")) {
                host.setHasParkView(true);
            }
        }
        if (hostDTO.getIsHasParkView() != null && hostDTO.getIsHasParkView() != "") {
            if (hostDTO.getIsHasParkView().equals("0")) {
                host.setHasParkView(false);
            }
            if (hostDTO.getIsHasParkView().equals("1")) {
                host.setHasParkView(true);
            }
        }
        if (hostDTO.getIsHasForestView() != null && hostDTO.getIsHasForestView() != "") {
            if (hostDTO.getIsHasForestView().equals("0")) {
                host.setHasForestView(false);
            }
            if (hostDTO.getIsHasForestView().equals("1")) {
                host.setHasForestView(true);
            }
        }
        if (hostDTO.getIsHasElevator() != null && hostDTO.getIsHasElevator() != "") {
            if (hostDTO.getIsHasElevator().equals("0")) {
                host.setHasElevator(false);
            }
            if (hostDTO.getIsHasElevator().equals("1")) {
                host.setHasElevator(true);
            }
        }
        if (hostDTO.getIsHasStair() != null && hostDTO.getIsHasStair() != "") {
            if (hostDTO.getIsHasStair().equals("0")) {
                host.setHasStair(false);
            }
            if (hostDTO.getIsHasStair().equals("1")) {
                host.setHasStair(true);
            }
        }
        if (hostDTO.getIsHasMeeting() != null && hostDTO.getIsHasMeeting() != "") {
            if (hostDTO.getIsHasMeeting().equals("0")) {
                host.setHasMeeting(false);
            }
            if (hostDTO.getIsHasMeeting().equals("1")) {
                host.setHasMeeting(true);
            }
        }
        if (hostDTO.getIsHasGarbageCollectionArea() != null && hostDTO.getIsHasGarbageCollectionArea() != "") {
            if (hostDTO.getIsHasGarbageCollectionArea().equals("0")) {
                host.setHasGarbageCollectionArea(false);
            }
            if (hostDTO.getIsHasGarbageCollectionArea().equals("1")) {
                host.setHasGarbageCollectionArea(true);
            }
        }
        if (hostDTO.getIsHas24HourReception() != null && hostDTO.getIsHas24HourReception() != "") {
            if (hostDTO.getIsHas24HourReception().equals("0")) {
                host.setHas24HourReception(false);
            }
            if (hostDTO.getIsHas24HourReception().equals("1")) {
                host.setHas24HourReception(true);
            }
        }
        if (hostDTO.getIsHasSmokingArea() != null && hostDTO.getIsHasSmokingArea() != "") {
            if (hostDTO.getIsHasSmokingArea().equals("0")) {
                host.setHasSmokingArea(false);
            }
            if (hostDTO.getIsHasSmokingArea().equals("1")) {
                host.setHasSmokingArea(true);
            }
        }
        if (hostDTO.getIsHasOutdoorParkingLot() != null && hostDTO.getIsHasOutdoorParkingLot() != "") {
            if (hostDTO.getIsHasOutdoorParkingLot().equals("0")) {
                host.setHasOutdoorParkingLot(false);
            }
            if (hostDTO.getIsHasOutdoorParkingLot().equals("1")) {
                host.setHasOutdoorParkingLot(true);
            }
        }
        if (hostDTO.getIsHasIndoorParkingLot() != null && hostDTO.getIsHasIndoorParkingLot() != "") {
            if (hostDTO.getIsHasIndoorParkingLot().equals("0")) {
                host.setHasIndoorParkingLot(false);
            }
            if (hostDTO.getIsHasIndoorParkingLot().equals("1")) {
                host.setHasIndoorParkingLot(true);
            }
        }
        if (hostDTO.getIsHasSharedCarPark() != null && hostDTO.getIsHasSharedCarPark() != "") {
            if (hostDTO.getIsHasSharedCarPark().equals("0")) {
                host.setHasSharedCarPark(false);
            }
            if (hostDTO.getIsHasSharedCarPark().equals("1")) {
                host.setHasSharedCarPark(true);
            }
        }
        if (hostDTO.getIsHasCarPark() != null && hostDTO.getIsHasCarPark() != "") {
            if (hostDTO.getIsHasCarPark().equals("0")) {
                host.setHasCarPark(false);
            }
            if (hostDTO.getIsHasCarPark().equals("1")) {
                host.setHasCarPark(true);
            }
        }
        if (hostDTO.getIsHasBilliard() != null && hostDTO.getIsHasBilliard() != "") {
            if (hostDTO.getIsHasBilliard().equals("0")) {
                host.setHasBilliard(false);
            }
            if (hostDTO.getIsHasBilliard().equals("1")) {
                host.setHasBilliard(true);
            }
        }
        if (hostDTO.getIsHasPingPong() != null && hostDTO.getIsHasPingPong() != "") {
            if (hostDTO.getIsHasPingPong().equals("0")) {
                host.setHasPingPong(false);
            }
            if (hostDTO.getIsHasPingPong().equals("1")) {
                host.setHasPingPong(true);
            }
        }
        if (hostDTO.getIsHasFishing() != null && hostDTO.getIsHasFishing() != "") {
            if (hostDTO.getIsHasFishing().equals("0")) {
                host.setHasFishing(false);
            }
            if (hostDTO.getIsHasFishing().equals("1")) {
                host.setHasFishing(true);
            }
        }
        if (hostDTO.getIsHasTennisCourse() != null && hostDTO.getIsHasTennisCourse() != "") {
            if (hostDTO.getIsHasTennisCourse().equals("0")) {
                host.setHasTennisCourse(false);
            }
            if (hostDTO.getIsHasTennisCourse().equals("1")) {
                host.setHasTennisCourse(true);
            }
        }
        if (hostDTO.getIsHasTerrace() != null && hostDTO.getIsHasTerrace() != "") {
            if (hostDTO.getIsHasTerrace().equals("0")) {
                host.setHasTerrace(false);
            }
            if (hostDTO.getIsHasTerrace().equals("1")) {
                host.setHasTerrace(true);
            }
        }
        if (hostDTO.getIsHasBBQAreaWithDiningTable() != null && hostDTO.getIsHasBBQAreaWithDiningTable() != "") {
            if (hostDTO.getIsHasBBQAreaWithDiningTable().equals("0")) {
                host.setHasBBQAreaWithDiningTable(false);
            }
            if (hostDTO.getIsHasBBQAreaWithDiningTable().equals("1")) {
                host.setHasBBQAreaWithDiningTable(true);
            }
        }
        if (hostDTO.getIsHasChildrenPlayArea() != null && hostDTO.getIsHasChildrenPlayArea() != "") {
            if (hostDTO.getIsHasChildrenPlayArea().equals("0")) {
                host.setHasChildrenPlayArea(false);
            }
            if (hostDTO.getIsHasChildrenPlayArea().equals("1")) {
                host.setHasChildrenPlayArea(true);
            }
        }
        if (hostDTO.getIsHasGarden() != null && hostDTO.getIsHasGarden() != "") {
            if (hostDTO.getIsHasGarden().equals("0")) {
                host.setHasGarden(false);
            }
            if (hostDTO.getIsHasGarden().equals("1")) {
                host.setHasGarden(true);
            }
        }
        if (hostDTO.getIsHasInfinityPool() != null && hostDTO.getIsHasInfinityPool() != "") {
            if (hostDTO.getIsHasInfinityPool().equals("0")) {
                host.setHasInfinityPool(false);
            }
            if (hostDTO.getIsHasInfinityPool().equals("1")) {
                host.setHasInfinityPool(true);
            }
        }
        if (hostDTO.getIsHasOutdoorPool() != null && hostDTO.getIsHasOutdoorPool() != "") {
            if (hostDTO.getIsHasOutdoorPool().equals("0")) {
                host.setHasOutdoorPool(false);
            }
            if (hostDTO.getIsHasOutdoorPool().equals("1")) {
                host.setHasOutdoorPool(true);
            }
        }
        if (hostDTO.getIsHasIndoorSwimmingPool() != null && hostDTO.getIsHasIndoorSwimmingPool() != "") {
            if (hostDTO.getIsHasIndoorSwimmingPool().equals("0")) {
                host.setHasIndoorSwimmingPool(false);
            }
            if (hostDTO.getIsHasIndoorSwimmingPool().equals("1")) {
                host.setHasIndoorSwimmingPool(true);
            }
        }
        if (hostDTO.getIsHasGym() != null && hostDTO.getIsHasGym() != "") {
            if (hostDTO.getIsHasGym().equals("0")) {
                host.setHasGym(false);
            }
            if (hostDTO.getIsHasGym().equals("1")) {
                host.setHasGym(true);
            }
        }
        if (hostDTO.getIsHasYoga() != null && hostDTO.getIsHasYoga() != "") {
            if (hostDTO.getIsHasYoga().equals("0")) {
                host.setHasYoga(false);
            }
            if (hostDTO.getIsHasYoga().equals("1")) {
                host.setHasYoga(true);
            }
        }
        if (hostDTO.getIsHasSauna() != null && hostDTO.getIsHasSauna() != "") {
            if (hostDTO.getIsHasSauna().equals("0")) {
                host.setHasSauna(false);
            }
            if (hostDTO.getIsHasSauna().equals("1")) {
                host.setHasSauna(true);
            }
        }
        if (hostDTO.getIsHasSpa() != null && hostDTO.getIsHasSpa() != "") {
            if (hostDTO.getIsHasSpa().equals("0")) {
                host.setHasSpa(false);
            }
            if (hostDTO.getIsHasSpa().equals("1")) {
                host.setHasSpa(true);
            }
        }
        if (hostDTO.getIsHasHairdressers() != null && hostDTO.getIsHasHairdressers() != "") {
            if (hostDTO.getIsHasHairdressers().equals("0")) {
                host.setHasHairdressers(false);
            }
            if (hostDTO.getIsHasHairdressers().equals("1")) {
                host.setHasHairdressers(true);
            }
        }
        if (hostDTO.getIsHasClinic() != null && hostDTO.getIsHasClinic() != "") {
            if (hostDTO.getIsHasClinic().equals("0")) {
                host.setHasClinic(false);
            }
            if (hostDTO.getIsHasClinic().equals("1")) {
                host.setHasClinic(true);
            }
        }
        if (hostDTO.getIsHasPharmacies() != null && hostDTO.getIsHasPharmacies() != "") {
            if (hostDTO.getIsHasPharmacies().equals("0")) {
                host.setHasPharmacies(false);
            }
            if (hostDTO.getIsHasPharmacies().equals("1")) {
                host.setHasPharmacies(true);
            }
        }
        if (hostDTO.getIsHasCoffeeShop() != null && hostDTO.getIsHasCoffeeShop() != "") {
            if (hostDTO.getIsHasCoffeeShop().equals("0")) {
                host.setHasCoffeeShop(false);
            }
            if (hostDTO.getIsHasCoffeeShop().equals("1")) {
                host.setHasCoffeeShop(true);
            }
        }
        if (hostDTO.getIsHasBar() != null && hostDTO.getIsHasBar() != "") {
            if (hostDTO.getIsHasBar().equals("0")) {
                host.setHasBar(false);
            }
            if (hostDTO.getIsHasBar().equals("1")) {
                host.setHasBar(true);
            }
        }
        if (hostDTO.getIsHasRestaurant() != null && hostDTO.getIsHasRestaurant() != "") {
            if (hostDTO.getIsHasRestaurant().equals("0")) {
                host.setHasRestaurant(false);
            }
            if (hostDTO.getIsHasRestaurant().equals("1")) {
                host.setHasRestaurant(true);
            }
        }
        if (hostDTO.getIsHasShoppingMall() != null && hostDTO.getIsHasShoppingMall() != "") {
            if (hostDTO.getIsHasShoppingMall().equals("0")) {
                host.setHasShoppingMall(false);
            }
            if (hostDTO.getIsHasShoppingMall().equals("1")) {
                host.setHasShoppingMall(true);
            }
        }
        if (hostDTO.getIsHasSouvenirStore() != null && hostDTO.getIsHasSouvenirStore() != "") {
            if (hostDTO.getIsHasSouvenirStore().equals("0")) {
                host.setHasSouvenirStore(false);
            }
            if (hostDTO.getIsHasSouvenirStore().equals("1")) {
                host.setHasSouvenirStore(true);
            }
        }
        if (hostDTO.getIsNearTheGolfCourse() != null && hostDTO.getIsNearTheGolfCourse() != "") {
            if (hostDTO.getIsNearTheGolfCourse().equals("0")) {
                host.setNearTheGolfCourse(false);
            }
            if (hostDTO.getIsNearTheGolfCourse().equals("1")) {
                host.setNearTheGolfCourse(true);
            }
        }
        if (hostDTO.getIsCloseToTheBusStop() != null && hostDTO.getIsCloseToTheBusStop() != "") {
            if (hostDTO.getIsCloseToTheBusStop().equals("0")) {
                host.setCloseToTheBusStop(false);
            }
            if (hostDTO.getIsCloseToTheBusStop().equals("1")) {
                host.setCloseToTheBusStop(true);
            }
        }
        if (hostDTO.getIsNearTheMuseum() != null && hostDTO.getIsNearTheMuseum() != "") {
            if (hostDTO.getIsNearTheMuseum().equals("0")) {
                host.setNearTheMuseum(false);
            }
            if (hostDTO.getIsNearTheMuseum().equals("1")) {
                host.setNearTheMuseum(true);
            }
        }
        if (hostDTO.getIsNearTheATM() != null && hostDTO.getIsNearTheATM() != "") {
            if (hostDTO.getIsNearTheATM().equals("0")) {
                host.setNearTheATM(false);
            }
            if (hostDTO.getIsNearTheATM().equals("1")) {
                host.setNearTheATM(true);
            }
        }
        if (hostDTO.getIsNearThePark() != null && hostDTO.getIsNearThePark() != "") {
            if (hostDTO.getIsNearThePark().equals("0")) {
                host.setNearThePark(false);
            }
            if (hostDTO.getIsNearThePark().equals("1")) {
                host.setNearThePark(true);
            }
        }
        if (hostDTO.getIsFreeBreakfast() != null && hostDTO.getIsFreeBreakfast() != "") {
            if (hostDTO.getIsFreeBreakfast().equals("0")) {
                host.setFreeBreakfast(false);
            }
            if (hostDTO.getIsFreeBreakfast().equals("1")) {
                host.setFreeBreakfast(true);
            }
        }
        if (hostDTO.getIsAirportTransfer() != null && hostDTO.getIsAirportTransfer() != "") {
            if (hostDTO.getIsAirportTransfer().equals("0")) {
                host.setAirportTransfer(false);
            }
            if (hostDTO.getIsAirportTransfer().equals("1")) {
                host.setAirportTransfer(true);
            }
        }
        if (hostDTO.getIsHasVehicleRental() != null && hostDTO.getIsHasVehicleRental() != "") {
            if (hostDTO.getIsHasVehicleRental().equals("0")) {
                host.setHasVehicleRental(false);
            }
            if (hostDTO.getIsHasVehicleRental().equals("1")) {
                host.setHasVehicleRental(true);
            }
        }
        if (hostDTO.getIsHasFreeBicycles() != null && hostDTO.getIsHasFreeBicycles() != "") {
            if (hostDTO.getIsHasFreeBicycles().equals("0")) {
                host.setHasFreeBicycles(false);
            }
            if (hostDTO.getIsHasFreeBicycles().equals("1")) {
                host.setHasFreeBicycles(true);
            }
        }
        if (hostDTO.getIsHasTicketingService() != null && hostDTO.getIsHasTicketingService() != "") {
            if (hostDTO.getIsHasTicketingService().equals("0")) {
                host.setHasTicketingService(false);
            }
            if (hostDTO.getIsHasTicketingService().equals("1")) {
                host.setHasTicketingService(true);
            }
        }
        if (hostDTO.getIsHasLaundry() != null && hostDTO.getIsHasLaundry() != "") {
            if (hostDTO.getIsHasLaundry().equals("0")) {
                host.setHasLaundry(false);
            }
            if (hostDTO.getIsHasLaundry().equals("1")) {
                host.setHasLaundry(true);
            }
        }
        if (hostDTO.getIsHasSubmitItemsBeforeCheckIn() != null && hostDTO.getIsHasSubmitItemsBeforeCheckIn() != "") {
            if (hostDTO.getIsHasSubmitItemsBeforeCheckIn().equals("0")) {
                host.setHasSubmitItemsBeforeCheckIn(false);
            }
            if (hostDTO.getIsHasSubmitItemsBeforeCheckIn().equals("1")) {
                host.setHasSubmitItemsBeforeCheckIn(true);
            }
        }
        host.setDateCanNotBookings(null);
        host.setAvailabilityType(true);
        host.setStatus(true);
        host.setApproved(false);
        host.setStars(0f);
        host.setTotalReview(0);
        return host;
    }

    @Override
    public BaseOutput delete(String hostId) {
        logger.info("HostService.delete");
        Host host = hostRepository.findById(Long.parseLong(hostId)).orElseThrow(()
                -> new ResourceNotFoundException("Host", "id", hostId));

        hostRepository.deleteHostByHostId(host.getId());
        Object object = ResponseEntity.ok().build();
        return CommonFunction.successOutput(object);
    }

}




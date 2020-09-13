package vn.com.pn.screen.f003Review.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.exception.ResourceInvalidInputException;
import vn.com.pn.screen.f003Review.dto.HostReviewByHostResDTO;
import vn.com.pn.screen.f003Review.dto.HostReviewDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.f002Booking.entity.Booking;
import vn.com.pn.screen.f003Review.dto.HostReviewWithUserDTO;
import vn.com.pn.screen.m002Host.entity.Host;
import vn.com.pn.screen.f003Review.entity.HostReview;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.f002Booking.repository.BookingRepository;
import vn.com.pn.screen.m002Host.repository.HostRepository;
import vn.com.pn.screen.f003Review.repository.HostReviewRepository;
import vn.com.pn.screen.f003Review.repository.HostReviewRepositoryCustomImpl;
import vn.com.pn.screen.f002Booking.service.BookingServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostReviewServiceImpl implements HostReviewService {
    private static Log logger = LogFactory.getLog(BookingServiceImpl.class);

    @Autowired
    private HostReviewRepository hostReviewRepository;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HostReviewRepositoryCustomImpl hostReviewRepositoryCustom;

    @Override
    public BaseOutput getAll() {
        logger.info("HostReviewServiceImpl.getAll");
        List<Object> listHostReview = new ArrayList<>(hostReviewRepository.findAll());
        return CommonFunction.successOutput(listHostReview);
    }

    @Override
    public BaseOutput insert(HostReviewDTO hostReviewDTO, User userLogin) {
        logger.info("HostReviewServiceImpl.insert");
        try {
            BaseOutput baseOutput = getInsertHostReviewInfo(hostReviewDTO, userLogin);
            return baseOutput;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @Override
    public BaseOutput getHostReviewByHost(String hostId) {
        logger.info("HostReviewServiceImpl.insert");
        try {
            Host host = hostRepository.findById(Long.parseLong(hostId)).orElseThrow(
                    () -> new ResourceNotFoundException("Host", "id", hostId)
            );
            List<HostReviewWithUserDTO> hostReviews = hostReviewRepositoryCustom.getHostReview(Long.parseLong(hostId));
            HostReviewByHostResDTO resDTO = new HostReviewByHostResDTO();
            resDTO.setStars(String.valueOf(host.getStars()));
            resDTO.setListReview(hostReviews);
            return CommonFunction.successOutput(resDTO);
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    private BaseOutput getInsertHostReviewInfo(HostReviewDTO hostReviewDTO, User userLogin) {
        logger.info("HostReviewServiceImpl.getInsertHostReviewInfo");
        HostReview hostReview = new HostReview();

        if (hostReviewDTO.getBookingId() != null && hostReviewDTO.getBookingId() != "") {
            Booking booking = bookingRepository.findById(Long.parseLong(hostReviewDTO.getBookingId())).orElseThrow(()
                    -> new ResourceNotFoundException("Booking", "id", hostReviewDTO.getBookingId()));
            if (userLogin.getId() != booking.getUser().getId()) {
                throw new ResourceInvalidInputException("Can not review. User login does not " +
                        "match with user has booked");
            }

            Host host = changeStarRating(booking.getHost().getId(), hostReviewDTO.getStarRating());
            hostRepository.save(host);

            hostReview.setBooking(booking);
        }
        if (hostReviewDTO.getContent() != null && hostReviewDTO.getContent() != "") {
            hostReview.setContent(hostReviewDTO.getContent());
        }
        if (hostReviewDTO.getStarRating() != null && hostReviewDTO.getStarRating() != "") {
            switch (Integer.parseInt(hostReviewDTO.getStarRating())) {
                case 1:
                    hostReview.setStarRating(1);
                    hostReview.setOutstanding(false);
                    break;
                case 2:
                    hostReview.setStarRating(2);
                    hostReview.setOutstanding(false);
                    break;
                case 3:
                    hostReview.setStarRating(3);
                    hostReview.setOutstanding(false);
                    break;
                case 4:
                    hostReview.setStarRating(4);
                    hostReview.setOutstanding(false);
                    break;
                case 5:
                    hostReview.setStarRating(5);
                    hostReview.setOutstanding(true);
                    break;
                default:
                    logger.error("Number not correct!");
            }
        }
        hostReview.setDelete(false);

        return CommonFunction.successOutput(hostReviewRepository.save(hostReview));
    }


    private Host changeStarRating(Long hostId, String starRating) {
        logger.info("HostReviewServiceImpl.changeStarRating");
        Host host = hostRepository.findById(hostId).orElseThrow(()
                -> new ResourceNotFoundException("Host", "id", hostId));
        List<Integer> listStarRating = hostReviewRepository.findStarRatingByHostId(hostId);

        listStarRating.add(Integer.parseInt(starRating));

        host.setStars(calculateAverage(listStarRating));
        host.setTotalReview(listStarRating.size());
        return host;
    }

    private float calculateAverage(List<Integer> listStarRating) {
        logger.info("HostReviewServiceImpl.calculateAverage");
        Integer sum = 0;
        if (!listStarRating.isEmpty()) {
            for (Integer star : listStarRating) {
                sum += star;
            }
            return sum.floatValue() / listStarRating.size();
        }
        return sum;
    }
}

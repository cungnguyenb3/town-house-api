package vn.com.pn.service.hostreview;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostReviewDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.Booking;
import vn.com.pn.domain.Host;
import vn.com.pn.domain.HostReview;
import vn.com.pn.domain.User;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.booking.BookingRepository;
import vn.com.pn.repository.host.HostRepository;
import vn.com.pn.repository.hostreview.HostReviewRepository;
import vn.com.pn.service.booking.BookingServiceImpl;

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

    @Override
    public BaseOutput getAll() {
        logger.info("BookingServiceImpl.insert");
        List<Object> listHostReview = new ArrayList<>(hostReviewRepository.findAll());
        return  CommonFunction.successOutput(listHostReview);
    }

    @Override
    public BaseOutput insert(HostReviewDTO hostReviewDTO, User userLogin) {
        logger.info("BookingServiceImpl.insert");
        try {
            HostReview hostReview = getInsertHostReviewInfo(hostReviewDTO, userLogin);
            return CommonFunction.successOutput(hostReviewRepository.save(hostReview));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    public List<Integer> getStarRatingByHost(String hostId){
        try {
            return hostReviewRepository.findStarRatingByHostId(Integer.parseInt(hostId));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return null;
        }
    }

    private HostReview getInsertHostReviewInfo(HostReviewDTO hostReviewDTO, User userLogin) {
        HostReview hostReview = new HostReview();
        if (hostReviewDTO.getHostId() != null && hostReviewDTO.getHostId() != ""){
            Host host = changeStarRating(hostReviewDTO.getHostId(),hostReviewDTO.getStarRating());
            hostRepository.save(host);
            hostReview.setHost(host);
        }
        if (userLogin != null) {
            hostReview.setUser(userLogin);
        }
        if (hostReviewDTO.getBookingId() != null && hostReviewDTO.getBookingId() != ""){
            Booking booking = bookingRepository.findById(Integer.parseInt(hostReviewDTO.getBookingId())).orElseThrow(()
                    -> new ResourceNotFoundException("Booking","id", hostReviewDTO.getBookingId()));
            hostReview.setBooking(booking);
        }
        if (hostReviewDTO.getContent() != null && hostReviewDTO.getContent() != "") {
            hostReview.setContent(hostReviewDTO.getContent());
        }
        if (hostReviewDTO.getStarRating() != null && hostReviewDTO.getStarRating() != "") {
            switch (Integer.parseInt(hostReviewDTO.getStarRating())){
                case 1:
                    hostReview.setStarRating(1);
                    break;
                case 2:
                    hostReview.setStarRating(2);
                    break;
                case 3:
                    hostReview.setStarRating(3);
                    break;
                case 4:
                    hostReview.setStarRating(4);
                    break;
                case 5:
                    hostReview.setStarRating(5);
                    break;
                default:
                    logger.error("Number not correct!");
            }
        }
        if (hostReviewDTO.getIsDelete() != null && hostReviewDTO.getIsDelete() != ""){
            if(hostReviewDTO.getIsDelete().equals("0")){
                hostReview.setDelete(false);
            }
            if(hostReviewDTO.getIsDelete().equals("1")){
                hostReview.setDelete(true);
            }
        }
        if (hostReviewDTO.getIsOutstanding() != null && hostReviewDTO.getIsOutstanding() != ""){
            if(hostReviewDTO.getIsOutstanding().equals("0")){
                hostReview.setOutstanding(false);
            }
            if(hostReviewDTO.getIsOutstanding().equals("1")){
                hostReview.setOutstanding(true);
            }
        }
        return hostReview;
    }


    private Host changeStarRating (String hostId, String starRating){
        Host host = hostRepository.findById(Integer.parseInt(hostId)).orElseThrow(()
                -> new ResourceNotFoundException("Host","id", hostId));
        List<Integer> listStarRating = getStarRatingByHost(hostId);
        listStarRating.add(Integer.parseInt(starRating));
        host.setStar(calculateAverage(listStarRating));
        return host;
    }

    private float calculateAverage(List <Integer> listStarRating) {
        Integer sum = 0;
        if(!listStarRating.isEmpty()) {
            for (Integer star : listStarRating) {
                sum += star;
            }
            return sum.floatValue() / listStarRating.size();
        }
        return sum;
    }
}

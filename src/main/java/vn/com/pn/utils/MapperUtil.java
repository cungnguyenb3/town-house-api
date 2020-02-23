package vn.com.pn.utils;

import org.modelmapper.ModelMapper;

public class MapperUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p> Map source data to destination </p>
     *
     * @param src
     * @param destination
     * @return destination
     */
    public static <D, S> D mapper(S src, Class<D> destination) {
        return modelMapper.map(src, destination);
    }
}

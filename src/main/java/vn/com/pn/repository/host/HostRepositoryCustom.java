package vn.com.pn.repository.host;

import vn.com.pn.domain.Host;

import java.util.List;

public interface HostRepositoryCustom {
    List<Host> search(String searchText, int pageNo, int resultsPerPage);
}

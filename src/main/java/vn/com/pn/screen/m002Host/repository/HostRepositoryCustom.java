package vn.com.pn.screen.m002Host.repository;

import vn.com.pn.screen.m002Host.entity.Host;

import java.util.List;

public interface HostRepositoryCustom {
    List<Host> search(String searchText, int pageNo, int resultsPerPage);
}

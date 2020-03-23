package vn.com.pn.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.User;
import vn.com.pn.security.AuthService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestRestAPIs {


	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private AuthService authService;

	@GetMapping("/api/test/user")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token",
					required = true, dataType = "string", paramType = "header") })
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public BaseOutput userAccess(HttpServletRequest request) {
		BaseOutput baseOutput = new BaseOutput();
		User user = authService.getLoggedUser();

		baseOutput.setStatus(1);
		baseOutput.setMessage("API đã thực hiện thành công");
		baseOutput.setTotalRecord(1);
		baseOutput.setData(user);

		String authHeader = request.getHeader("Authorization");
		return baseOutput;
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token",
					required = true, dataType = "string", paramType = "header") })
	@GetMapping("/api/test/super-admin")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
	public String projectManagementAccess() {
		return ">>> Board Management Project";
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token",
					required = true, dataType = "string", paramType = "header") })
	@GetMapping("/api/test/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String supperAdminAccess() {
		return ">>> Admin Contents";
	}
}
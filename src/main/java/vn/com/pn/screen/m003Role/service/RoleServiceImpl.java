package vn.com.pn.screen.m003Role.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.exception.ResourceInvalidInputException;
import vn.com.pn.screen.m003Role.dto.RoleDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m003Role.entity.Role;
import vn.com.pn.screen.m003Role.entity.RoleName;
import vn.com.pn.screen.m003Role.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private static Log logger = LogFactory.getLog(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("RoleService.getAll");
        List<Object> listRoles = new ArrayList<>(roleRepository.findAll());
        return CommonFunction.successOutput(listRoles);
    }

    @Override
    public BaseOutput insert(RoleDTO roleDTO) {
        logger.info("RoleService.insert");
        try {
            Role role = new Role();
            if (roleDTO.getName() != null && roleDTO.getName() != "") {
                switch (roleDTO.getName()) {
                    case "admin":
                        role.setName(RoleName.ROLE_ADMIN);
                        break;
                    case "super-admin":
                        role.setName(RoleName.ROLE_SUPER_ADMIN);
                        break;
                    case "agent":
                        role.setName(RoleName.ROLE_HOST_AGENT);
                        break;
                    default:
                        role.setName(RoleName.ROLE_USER);
                }
            }
            return CommonFunction.successOutput(roleRepository.save(role));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }
}

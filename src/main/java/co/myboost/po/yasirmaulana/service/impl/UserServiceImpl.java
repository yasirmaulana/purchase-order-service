package co.myboost.po.yasirmaulana.service.impl;

import co.myboost.po.yasirmaulana.domain.User;
import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;
import co.myboost.po.yasirmaulana.dto.UserCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.UserListResponseDTO;
import co.myboost.po.yasirmaulana.exception.NotFoundException;
import co.myboost.po.yasirmaulana.repository.UserRepository;
import co.myboost.po.yasirmaulana.service.UserService;
import co.myboost.po.yasirmaulana.util.PaginationUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String INVALID_USERID = "invalid.user.id";
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserCreateUpdateRequestDTO dto) {
        User user = mapDtoToUser(dto);
        userRepository.save(user);
    }

    @Override
    public void updateUser(int userId, UserCreateUpdateRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(INVALID_USERID));

        user.setFirstName(Optional.ofNullable(dto.getFirstName()).orElse(user.getFirstName()));
        user.setLastName(Optional.ofNullable(dto.getLastName()).orElse(user.getLastName()));
        user.setEmail(Optional.ofNullable(dto.getEmail()).orElse(user.getEmail()));
        user.setPhone(Optional.ofNullable(dto.getPhone()).orElse(user.getPhone()));

        userRepository.save(user);
    }

    @Override
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(INVALID_USERID));
        userRepository.delete(user);
    }

    @Override
    public User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(INVALID_USERID));
    }

    @Override
    public ResultPageResponseDTO<UserListResponseDTO> getUserList(Integer pages, Integer limit,
                                                                  String sortBy, String direction, String firstName) {
        firstName = StringUtils.isBlank(firstName)?"%":firstName+"%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<User> pageResult = userRepository.findByFirstNameLikeIgnoreCase(firstName, pageable);
        List<UserListResponseDTO> dtos = pageResult.stream().map(p -> {
            UserListResponseDTO dto = new UserListResponseDTO();
            dto.setId(p.getId());
            dto.setFirstName(p.getFirstName());
            dto.setLastName(p.getLastName());
            dto.setEmail(p.getEmail());
            dto.setPhone(p.getPhone());

            return dto;
        }).toList();
        return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
    }

    private User mapDtoToUser(UserCreateUpdateRequestDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        return user;
    }
}

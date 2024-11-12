package co.myboost.po.yasirmaulana.service;

import co.myboost.po.yasirmaulana.domain.User;
import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;
import co.myboost.po.yasirmaulana.dto.UserCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.UserListResponseDTO;

public interface UserService {
    void createUser(UserCreateUpdateRequestDTO dto);
    void updateUser(int userId, UserCreateUpdateRequestDTO dto);
    void deleteUser(int userId);
    User getUserById(int userId);
    ResultPageResponseDTO<UserListResponseDTO> getUserList(Integer pages, Integer limit, String sortBy,
                                                                String direction, String firstName);

}

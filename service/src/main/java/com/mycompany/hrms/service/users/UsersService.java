package com.mycompany.hrms.service.users;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.users.response.AuthResponse;
import com.mycompany.hrms.data.dtos.users.response.EventRes;
import com.mycompany.hrms.data.dtos.users.response.OrgChartRes;
import com.mycompany.hrms.data.dtos.users.response.UserListRes;
import com.mycompany.hrms.data.entity.game.GameConfig;
import com.mycompany.hrms.data.entity.game.GameSlots;
import com.mycompany.hrms.data.entity.user.Departments;
import com.mycompany.hrms.data.entity.user.Roles;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.game.GameConfigRepo;
import com.mycompany.hrms.data.repository.game.GameSlotsRepo;
import com.mycompany.hrms.data.repository.users.DepartmentsRepo;
import com.mycompany.hrms.data.repository.users.RolesRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.data.dtos.game.response.GameSlotResponse;
import com.mycompany.hrms.data.dtos.users.request.UpdateUserProfileDto;
import com.mycompany.hrms.data.dtos.users.request.UpdateUserProfileHr;
import com.mycompany.hrms.data.dtos.users.request.UserProfileCreate;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.IErrorMessages;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UsersService implements IUserService {

    private final UsersRepo usersRepo;
    private final ModelMapper modelMapper;
    private final RolesRepo rolesRepo;
    private final DepartmentsRepo departmentsRepo;
    private final PasswordEncoder passwordEncoder;
    private final GameSlotsRepo gameSlotsRepo;
    private final GameConfigRepo gameConfigRepo;

    @Autowired
    public UsersService(
            UsersRepo usersRepo,
            ModelMapper modelMapper,
            RolesRepo rolesRepo,
            DepartmentsRepo departmentsRepo,
            PasswordEncoder passwordEncoder,
            GameSlotsRepo gameSlotsRepo,
            GameConfigRepo gameConfigRepo
    ){
        this.usersRepo = usersRepo;
        this.modelMapper = modelMapper;
        this.rolesRepo = rolesRepo;
        this.departmentsRepo = departmentsRepo;
        this.passwordEncoder = passwordEncoder;
        this.gameSlotsRepo = gameSlotsRepo;
        this.gameConfigRepo = gameConfigRepo;
    }

    public List<com.mycompany.hrms.data.dtos.users.response.UserProfileDto> getAllUserProfiles(Pageable pageable, Long department){
        if(department!=null){
            return usersRepo.findAllByDepartment_DepartmentId(department, pageable).stream().map(val -> modelMapper.map(val, com.mycompany.hrms.data.dtos.users.response.UserProfileDto.class)).toList();
        }else{
            return usersRepo.findAll().stream().map(val -> modelMapper.map(val, com.mycompany.hrms.data.dtos.users.response.UserProfileDto.class)).toList();
        }
    }

    public com.mycompany.hrms.data.dtos.users.response.UserProfileDto getUserProfileByUserId(long userId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        return modelMapper.map(user, com.mycompany.hrms.data.dtos.users.response.UserProfileDto.class);
    }

    public List<EventRes> getUsersWithBirthday(){
        return usersRepo.findUsersWithBirthdayToday().stream().map(val -> modelMapper.map(val, EventRes.class)).toList();
    }

    public List<UserListRes> getUsersListByName(String name){
        List<Users> users =usersRepo.findUsersByNameLike(name + "%");
        return users.stream().map(val -> modelMapper.map(val, UserListRes.class)).toList();
    }

    public List<com.mycompany.hrms.data.dtos.users.response.UserProfileDto> getUsersProfileByName(String name, Long departmentId){
        if(departmentId != null)
            return usersRepo.findAllByDepartment_DepartmentIdAndNameStartingWith(departmentId, name)
                    .stream()
                    .map(val -> modelMapper.map(val, com.mycompany.hrms.data.dtos.users.response.UserProfileDto.class)).toList();
        else
            return usersRepo.findByNameStartingWith(name)
                    .stream()
                    .map(val -> modelMapper.map(val, com.mycompany.hrms.data.dtos.users.response.UserProfileDto.class)).toList();
    }

    public List<String> getAllDesignations(){
        return Arrays.stream(Constants.Designation.values()).map(Constants.Designation::name).toList();
    }

    public AuthResponse getUserRole(String email){
        Users user = usersRepo.findUsersByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        AuthResponse response = modelMapper.map(user, AuthResponse.class);
        response.setRole(user.getRole().getName());
        return response;
    }

    public OrgChartRes getOrgChart(long userId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        OrgChartRes res = modelMapper.map(user, OrgChartRes.class);
        if(user.getAssignedUnder()!=null)
            res.setAssignedUnder(user.getAssignedUnder().getUserId());
        else
            res.setAssignedUnder(0);
        return res;
    }

    public List<OrgChartRes> getOrgChartList(long userId){
        List<OrgChartRes> res = new ArrayList<>();
        OrgChartRes temp = getOrgChart(userId);
        res.add(temp);
        while (temp.getAssignedUnder() != 0){
            temp = getOrgChart(temp.getAssignedUnder());
            res.add(temp);
        }
        Collections.reverse(res);
        return res;
    }

    @Transactional
    public void makeGameFavourite(long userId, long gameId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        GameConfig game = gameConfigRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));

        if(user.getFavoriteGame() != null)
            throw new BadRequestException("Favourite game already configured");
        if(user.getFavoriteGame()!=null&&user.getFavoriteGame().getGameId() == gameId)
            throw new BadRequestException("Already set as favourite game");
        user.setFavoriteGame(game);
        game.getLikedBy().add(user);
    }
    @Transactional
    public void updateUserProfile(long userId, UpdateUserProfileHr updatedProfile){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        Roles roles = rolesRepo.findById(updatedProfile.getRoleId())
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid role type"));
        Departments departments = departmentsRepo.findById(updatedProfile.getDepartmentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid department"));
        Users assignedUnder = usersRepo.findById(updatedProfile.getAssignedUnderId())
                        .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
        if(usersRepo.checkUserAssign(assignedUnder.getUserId(), user.getUserId())){
            throw new BadRequestException("Can not assign under provided user");
        }
        user.setRole(roles);
        user.setDepartment(departments);
        user.setUpdatedAt(ZonedDateTime.now());
        user.setAssignedUnder(assignedUnder);
        modelMapper.map(updatedProfile, user);
    }

    @Transactional
    public void removeGameFromFavourite(long userId, long gameId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        GameConfig game = gameConfigRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        if(user.getFavoriteGame().getGameId() != gameId)
            throw new BadRequestException("Already set as favourite game");
        if(user.getFavoriteGame() == null)
            throw new BadRequestException("No favourite games");
        user.setFavoriteGame(null);
        game.getLikedBy().remove(user);
    }

    public com.mycompany.hrms.data.dtos.users.response.FavouriteGameResponse getSlotsOfFavouriteGame(long userId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        if(user.getFavoriteGame() == null)
            throw new BadRequestException("Favourite game not configured yet");
        List<GameSlots> upComingSlots = gameSlotsRepo.getTop5LatestSlots(user.getFavoriteGame().getGameId());
        com.mycompany.hrms.data.dtos.users.response.FavouriteGameResponse response = new com.mycompany.hrms.data.dtos.users.response.FavouriteGameResponse();
        response.setName(user.getFavoriteGame().getName());
        response.setGameId(user.getFavoriteGame().getGameId());
        response.setUpComingSlots(upComingSlots.stream().map(val -> modelMapper.map(val, GameSlotResponse.class)).toList());
        return response;
    }

    public List<OrgChartRes> getAssignedUnder(long userId){
        return usersRepo.findUsersByAssignedUnder_UserId(userId).stream().map(val -> modelMapper.map(val, OrgChartRes.class)).toList();
    }

    public com.mycompany.hrms.data.dtos.users.response.UserProfileDto getUserProfileByEmail(String email){
        return modelMapper.map(usersRepo.findUsersByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND)), com.mycompany.hrms.data.dtos.users.response.UserProfileDto.class);
    }

    @Transactional
    public com.mycompany.hrms.data.dtos.users.response.UserProfileCreated createUserProfile(UserProfileCreate userProfileCreate){
        Users users = modelMapper.map(userProfileCreate, Users.class);
        users.setRole(rolesRepo.findById(userProfileCreate.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found in database")));
        users.setDepartment(departmentsRepo.findById(userProfileCreate.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found in database")));
        Users assignedUnder = usersRepo.findById(userProfileCreate.getAssignUnderId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for assign under"));
        long years = ChronoUnit.YEARS.between(userProfileCreate.getBirthdate(), ZonedDateTime.now());
        if(years<18)
            throw new BadRequestException("Invalid birthdate");
        users.setPassword(passwordEncoder.encode(userProfileCreate.getPassword()));
        Users newProfile = usersRepo.save(users);
        newProfile.setAssignedUnder(assignedUnder);
        return modelMapper.map(newProfile, com.mycompany.hrms.data.dtos.users.response.UserProfileCreated.class);
    }

    @Transactional
    public void updateActiveStatus(long userId, boolean status){
        Users users = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        users.setActive(status);
    }

    public boolean isBlocked(String email){
        return !usersRepo.findUsersByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).isActive();
    }

    @Transactional
    public com.mycompany.hrms.data.dtos.users.response.UpdatedUserProfileDto updateUserProfile(long userId, UpdateUserProfileDto updatedProfile){
        Users profile = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        modelMapper.map(updatedProfile, profile);
        profile.setUpdatedAt(ZonedDateTime.now());
        return  modelMapper.map(profile, com.mycompany.hrms.data.dtos.users.response.UpdatedUserProfileDto.class);
    }

    public void deleteUser(long userId){
        if(!usersRepo.existsById(userId))
            throw new ResourceNotFoundException("User does not exist");
        usersRepo.deleteById(userId);
    }
}

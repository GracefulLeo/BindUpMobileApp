package com.example.rrty6.vcardapp.data.managers;

import android.os.Handler;
import android.os.Message;

import com.example.rrty6.vcardapp.data.network.model.req.CreateCardReq;
import com.example.rrty6.vcardapp.data.network.model.req.CreateGroupReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateCardReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateCardsReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateContactsReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateGroupReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateGroupsReq;
import com.example.rrty6.vcardapp.data.network.model.req.UploadLogoReq;
import com.example.rrty6.vcardapp.data.network.model.req.UserRegisterReq;
import com.example.rrty6.vcardapp.data.network.model.res.CreateCardRes;
import com.example.rrty6.vcardapp.data.network.model.res.CreateGroupRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetAllCardRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetCardRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetFileRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetGroupRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetUserRes;
import com.example.rrty6.vcardapp.data.network.model.res.LoginRes;
import com.example.rrty6.vcardapp.data.network.model.res.TokenRes;
import com.example.rrty6.vcardapp.data.network.RestService;
import com.example.rrty6.vcardapp.data.network.ServiceGenerator;
import com.example.rrty6.vcardapp.data.network.model.req.UserLoginReq;
import com.example.rrty6.vcardapp.data.network.model.res.UploadLogoRes;
import com.example.rrty6.vcardapp.data.storage.model.Base;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.data.storage.model.GroupCard;
import com.example.rrty6.vcardapp.data.storage.model.Logo;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.data.storage.model.SocialLink;
import com.example.rrty6.vcardapp.utils.App;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

//Dealer for PreferenceManager, Retrofit and ORMLite
public class DataManager {
    private static DataManager INSTANCE = new DataManager();//For singleton(probably, will be transfered to Application)

    private PreferencesManager mPreferenceManager;
    private RestService mRestService;//Network (unused, for Retrofit)

    //region===========================DAO Declaration============================
    private Dao<Card, Long> cardDao;
    private Dao<Email, Long> emailDao;
    private Dao<Phone, Long> phoneDao;
    private Dao<SocialLink, Long> socialLinkDao;
    private Dao<Logo, Long> logoDao;
    private Dao<Base, Long> baseDao;
    private Dao<Group, Long> groupDao;
    private Dao<GroupCard, Long> groupCardDao;

    //endregion========================DAO Declaration============================

    private DataManager() {
        this.mPreferenceManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);

        //region===========================DAO init============================
        this.cardDao = App.getCardDao();
        this.phoneDao = App.getPhoneDao();
        this.emailDao = App.getEmailDao();
        this.socialLinkDao = App.getSocialLinkDao();
        this.logoDao = App.getLogoDao();
        this.baseDao = App.getBaseDao();
        this.groupDao = App.getGroupDao();
        this.groupCardDao = App.getGroupCardDao();
        //endregion========================DAO init============================
    }

    //region =================== Utils =================================
    public static DataManager getInstance() {
        return INSTANCE;
    }

    public PreferencesManager getPreferenceManager() {
        return mPreferenceManager;
    }

    public boolean isAuthorized() {
        return mPreferenceManager.loadCookie() != null && !mPreferenceManager.loadCookie().isEmpty() &&
                mPreferenceManager.loadToken() != null && !mPreferenceManager.loadToken().isEmpty() &&
                mPreferenceManager.loadUserID() != null && !mPreferenceManager.loadUserID().isEmpty();
    }

    public void handleError(Handler handler) {
        Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);//Finish: Handle error
    }

    public void handleException(/*Handler handler, Exception e*/) {
//        Message message = new Message();
//        message.what = 0;
//        message.obj = e;
//        handler.sendMessage(message);//Finish: Handle exception
    }

    //endregion ================ Utils =================================

    //region ================ Network ===================================
    public Call<ResponseBody> register(UserRegisterReq userRegisterReq) {
        if (!isAuthorized()) {
            return mRestService.register(userRegisterReq);
        } else {
            return null;//TODO: обработать исключение запроса REGISTER
        }
    }

    public Call<LoginRes> logginUser(UserLoginReq userLoginReq) {
        if (!isAuthorized()) {
            return mRestService.loginUser(userLoginReq);
        } else {
            return null;//TODO: обработать исключение запроса LOGIN
        }
    }

    //stub action(not necessary)
    public Call<TokenRes> getToken() {
        return mRestService.getToken(mPreferenceManager.loadCookie());
    }

    public Call<ResponseBody> logOut() {
        if (isAuthorized()) {
            Call<ResponseBody> call = mRestService.logOut(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken());
//            mPreferenceManager.logoutUser();
            return call;
        } else {
            return null;//TODO: обработать исключение запроса LOGOUT
        }
    }

    //Receives one by one according to getAllCards list
    public Call<GetCardRes> getCard(String cardNumber) {
        if (isAuthorized()) {
            return mRestService.getCard(mPreferenceManager.loadCookie(), cardNumber);
        } else {
            return null;//TODO: обработать исключение запроса GET CARD
        }

    }

    //Receives all UserRes's cards list
    public Call<List<GetAllCardRes>> getAllCards() {
        if (isAuthorized()) {
            return mRestService.getAllCards(mPreferenceManager.loadCookie(), mPreferenceManager.loadUserID(), "nid");
        } else {
            return null;
        }
    }

    //Receives logo for card or group by fid
    public Call<GetFileRes> getLogo(String fid) {
        if (isAuthorized()) {
            return mRestService.downloadLogo(mPreferenceManager.loadCookie(), fid);
        } else {
            return null;
        }
    }

    public Call<GetGroupRes> getGroup(String id) {
        if (isAuthorized()) {
            return mRestService.downloadGroup(mPreferenceManager.loadCookie(), id);
        } else {
            return null;
        }
    }

    //Sends logo for card or group (defined by filepath in request model)
    public Call<UploadLogoRes> sendLogo(UploadLogoReq uploadLogoReq) {
        if (isAuthorized()) {
            return mRestService.uploadLogo(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken(), uploadLogoReq);
        } else {
            return null;
        }
    }

    public Call<CreateCardRes> sendCard(CreateCardReq createCardReq) {
        if (isAuthorized()) {
            return mRestService.createCard(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken(), createCardReq);
        } else {
            return null;
        }
    }

    public Call<CreateGroupRes> sendGroup(CreateGroupReq createGroupReq) {
        if (isAuthorized()) {
            return mRestService.createGroup(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken(), createGroupReq);
        } else {
            return null;
        }
    }

    public Call<GetUserRes> getUser() {
        if (isAuthorized()) {
            return mRestService.getUser(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken(), mPreferenceManager.loadUserID());
        } else {
            return null;
        }
    }

    public Call<ResponseBody> updateUser(UpdateCardsReq req) {
        if (isAuthorized()) {
            return mRestService.updateUser(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken(), mPreferenceManager.loadUserID(), req);
        } else {
            return null;
        }
    }

    public Call<ResponseBody> updateUser(UpdateContactsReq req) {
        if (isAuthorized()) {
            return mRestService.updateUser(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken(), mPreferenceManager.loadUserID(), req);
        } else {
            return null;
        }
    }

    public Call<ResponseBody> updateUser(UpdateGroupsReq req) {
        if (isAuthorized()) {
            return mRestService.updateUser(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken(), mPreferenceManager.loadUserID(), req);
        } else {
            return null;
        }
    }

    public Call<ResponseBody> updateCard(UpdateCardReq req, String cardId) {
        if (isAuthorized()) {
            return mRestService.updateCard(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken(), cardId, req);
        } else {
            return null;
        }
    }
    public Call<ResponseBody> updateGroup(UpdateGroupReq req) {
        if (isAuthorized()) {
            return mRestService.updateGroup(mPreferenceManager.loadCookie(), mPreferenceManager.loadToken(), req.getId(), req);
        } else {
            return null;
        }
    }

    //endregion ============= Network ===================================

    //region===========DataBase=================================

    //Saves Card
    //NEEDS PRE-STORED LOGO AND BASE_64
    //DOESN'T SAVE EMAIL AND PHONES
    public void addCard(Card card) {
        try {
            cardDao.create(card);
        } catch (SQLException e) {
            handleException();
        }
    }

    public void updateCard(Card card) {
        if (card.isMy()) {
            try {
                cardDao.update(card);
            } catch (SQLException e) {
                handleException();
            }
        }
    }

    public void updateLogo(Logo logo) {
        try {
            logoDao.update(logo);
        } catch (SQLException e) {
            handleException();
        }
    }

    //Returns all MY!!! cards from DB
    public List<Card> getCardList() {
        List<Card> cards = null;
        try {
            cards = cardDao.queryForAll();
            List<Card> ecept = new ArrayList<>();
            for (Card card : cards) {
                if (!card.isMy()) {
                    ecept.add(card);
                }
            }
            cards.removeAll(ecept);
        } catch (SQLException e) {
            handleException();
        }
        return cards;
    }

    public List<Card> getAllCardsFromDB() {
        try {
            return cardDao.queryForAll();
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    public Card getCard(Card card) {
        try {
            return cardDao.queryForSameId(card);
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    public Card getCardFromDB(String remoteId) {
        try {
            QueryBuilder<Card, Long> queryBuilder = cardDao.queryBuilder();
            queryBuilder.where().eq("REMOTE_ID", remoteId);
            PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
            return (Card) (cardDao.query(preparedQuery) != null && cardDao.query(preparedQuery).size() > 0 ? cardDao.query(preparedQuery).get(0) : null);
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    //Returns specified card
    //NOT TESTED
    public Card getCardById(long id) {
        try {
            return cardDao.queryForId(id);
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    //NOT TESTED
    public void deleteCard(Card card) {
        try {
            cardDao.delete(card);
        } catch (SQLException e) {
            handleException();
        }
    }

    //Saves Email
    //One by One!!!
    //NEEDS PRE-STORED CARD
    public void addEmail(Email email) {
        try {
            emailDao.create(email);
        } catch (SQLException e) {
            handleException();
        }
    }

    public List<Email> getAllEmails() {
        try {
            return emailDao.queryForAll();
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    public List<Phone> getAllPhones() {
        try {
            return phoneDao.queryForAll();
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    public List<Logo> getAllLogos() {
        try {
            return logoDao.queryForAll();
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }


    public List<Base> getAllBases() {
        try {
            return baseDao.queryForAll();
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    public List<GroupCard> getAllGroupCards() {
        try {
            return groupCardDao.queryForAll();
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    //Saves collection of emails
    public void addEmails(Collection<Email> emails) {
        try {
            if (emails != null && emails.size() != 0) {
                for (Email email : emails) {
                    emailDao.create(email);
                }
            }
        } catch (SQLException e) {
            handleException();
        }
    }

    public void deleteEmails(Collection<Email> emails) {
        try {
            if (emails != null && emails.size() > 0) {
                for (Email email : emails) {
                    emailDao.delete(email);
                }
            }
        } catch (SQLException e) {
            handleException();
        }
    }

    //Saves Phone
    //One by One!!!
    //NEEDS PRE-STORED CARD
    public void addPhone(Phone phone) {
        try {
            phoneDao.create(phone);
        } catch (SQLException e) {
            handleException();
        }
    }

    //Saves collection of phones
    public void addPhones(Collection<Phone> phones) {
        try {
            if (phones != null && phones.size() > 0) {
                for (Phone phone : phones) {
                    phoneDao.create(phone);
                }
            }
        } catch (SQLException e) {
            handleException();
        }
    }

    public void deletePhones(Collection<Phone> phones) {
        try {
            if (phones != null && phones.size() != 0) {
                for (Phone phone : phones) {
                    phoneDao.delete(phone);
                }
            }
        } catch (SQLException e) {
            handleException();
        }
    }

    //Saves Logo(only "fid" yet)TODO: download logo
    //NEEDED FOR SAVING CARD
    public void addLogo(Logo logo) {
        try {
            logoDao.create(logo);
        } catch (SQLException e) {
            handleException();
        }
    }

//    }

    public void deleteLogo(Logo logo) {
        try {
            logoDao.delete(logo);
        } catch (SQLException e) {
            handleException();
        }
    }

    public void addSocialLink(SocialLink link) {
        try {
            socialLinkDao.create(link);
        } catch (SQLException e) {
            handleException();
        }
    }

    public void addSocialLinks(Collection<SocialLink> socialLinks) {
        try {
            if (socialLinks != null && socialLinks.size() > 0) {
                for (SocialLink link : socialLinks) {
                    socialLinkDao.create(link);
                }
            }
        } catch (SQLException e) {
            handleException();
        }
    }

    //Saves Base
    //NEEDED FOR SAVING CARD
    public void addBase64(Base base) {
        try {
            baseDao.create(base);
        } catch (SQLException e) {
            handleException();
        }
    }

    public void deleteBase64(Base base) {
        try {
            baseDao.delete(base);
        } catch (SQLException e) {
            handleException();
        }
    }

    public List<Card> getContactList() {
        try {
            List<Card> cards = cardDao.queryForAll();
            List<Card> ecept = new ArrayList<>();
            for (Card card : cards) {
                if (!card.isMy()) {
                    ecept.add(card);
                }
            }
            return ecept;
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    //region=====================================Group=====================================

    public void addGroup(Group group) {
        try {
            groupDao.create(group);
        } catch (SQLException e) {
            handleException();
        }
    }

    public void addContactToGroup(Group group, Card card) {
        try {
            groupCardDao.create(new GroupCard(group, card));
        } catch (SQLException e) {
            handleException();
        }
    }

    public void deleteGroup(Group group) {
        try {
            groupDao.delete(group);
        } catch (SQLException e) {
            handleException();
        }
    }

    public void deleteContactFromGroup(Group group, Card card) {
        try {
            groupCardDao.delete(new GroupCard(group, card));
        } catch (SQLException e) {
            handleException();
        }
    }

    public List<Group> getGroupList() {
        try {
            return groupDao.queryForAll();
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    public List<Card> getGroupContacts(Group group) {
        try {
            PreparedQuery<Card> cardsForGroupQuery = makeCardsForGroupQuery();
            cardsForGroupQuery.setArgumentHolderValue(0, group);
            return cardDao.query(cardsForGroupQuery);
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    public void updateGroup(Group group) {
        try {
            groupDao.update(group);
        } catch (SQLException e) {
            handleException();
        }
    }

    public void deleteGroupCard(GroupCard groupCard) {
        try {
            groupCardDao.delete(groupCard);
        } catch (SQLException e) {
            handleException();
        }
    }

    private PreparedQuery<Card> makeCardsForGroupQuery() throws SQLException {
        // build our inner query for GroupCard objects
        QueryBuilder<GroupCard, Long> groupCardQb = groupCardDao.queryBuilder();
        // just select the card-id field
        groupCardQb.selectColumns("card_id");
        SelectArg userSelectArg = new SelectArg();

        groupCardQb.where().eq("group_id", userSelectArg);

        // build our outer query for Card objects
        QueryBuilder<Card, Long> cardQb = cardDao.queryBuilder();
        // where the id matches in the card-id from the inner query
        cardQb.where().in("id", groupCardQb);
        return cardQb.prepare();
    }

    public Group getGroup(Long id) {
        try {
            return groupDao.queryForId(id);
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }

    public Group getGroupByRemoteId(String remoteId) {
        try {
            QueryBuilder<Group, Long> queryBuilder = groupDao.queryBuilder();
            queryBuilder.where().eq("REMOTE_ID", remoteId);
            PreparedQuery<Group> preparedQuery = queryBuilder.prepare();
            return (Group) (groupDao.query(preparedQuery) != null && groupDao.query(preparedQuery).size() > 0 ? groupDao.query(preparedQuery).get(0) : null);
        } catch (SQLException e) {
            handleException();
            return null;
        }
    }
    //endregion=====================================Group=====================================

    //endregion========DataBase=================================
}

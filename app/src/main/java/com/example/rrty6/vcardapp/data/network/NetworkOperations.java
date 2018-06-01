package com.example.rrty6.vcardapp.data.network;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.managers.DataManager;
import com.example.rrty6.vcardapp.data.network.model.req.CreateCardReq;
import com.example.rrty6.vcardapp.data.network.model.req.CreateGroupReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateCardReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateCardsReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateContactsReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateGroupReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateGroupsReq;
import com.example.rrty6.vcardapp.data.network.model.req.UploadLogoReq;
import com.example.rrty6.vcardapp.data.network.model.req.UploadLogoReq.*;
import com.example.rrty6.vcardapp.data.network.model.req.UserRegisterReq;
import com.example.rrty6.vcardapp.data.network.model.res.CreateGroupRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetGroupRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetUserRes;
import com.example.rrty6.vcardapp.data.network.model.req.UserLoginReq;
import com.example.rrty6.vcardapp.data.network.model.res.CreateCardRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetCardRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetFileRes;
import com.example.rrty6.vcardapp.data.network.model.res.LoginRes;
import com.example.rrty6.vcardapp.data.network.model.SimpleUnd;
import com.example.rrty6.vcardapp.data.network.model.res.TokenRes;
import com.example.rrty6.vcardapp.data.network.model.res.UploadLogoRes;
import com.example.rrty6.vcardapp.data.storage.model.Base;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.data.storage.model.Logo;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.data.storage.model.SocialLink;
import com.example.rrty6.vcardapp.data.storage.operation.DatabaseOperation;
import com.example.rrty6.vcardapp.utils.App;
import com.example.rrty6.vcardapp.utils.Const.ListRole;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rrty6.vcardapp.utils.Const.*;

public class NetworkOperations {
    private static final String TAG = "NetworkOperations";
    private static DataManager mDataManager = DataManager.getInstance();

    public void register(final Handler handler, final String email, final String password, final MainOperations mainOperations) {
        Log.i(TAG, "register start");
        Call<ResponseBody> call = mDataManager.register(new UserRegisterReq(email, password));
        Response<ResponseBody> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                signIn(email, password, mainOperations);
            } else {
                if (response.code() == 406 && response.message().equals("Not Acceptable : The e-mail address test@user.com is already registered. Have you forgotten your password?")) {
                    Log.e(TAG, "The e-mail address " + email + " is already registered");
                    handler.sendEmptyMessage(0);//Finish: already used email
                } else {
                    Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
                }
            }
        } else {
            Log.e(TAG, "Null response");
        }
    }

    public void signIn(String email, String password, final MainOperations mainOperations) {
        Log.i(TAG, "signIn start");
        Call<LoginRes> call = mDataManager.logginUser(new UserLoginReq(email, password));
        Response<LoginRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    //Gets session
                    mDataManager.getPreferenceManager().saveCookie(response.headers().get("Set-Cookie"));
                    //Gets token
                    mDataManager.getPreferenceManager().saveToken(response.body().getToken());
                    //Gets UserRes Id
                    mDataManager.getPreferenceManager().saveUserID(response.body().getUser().getUid());
                    //Gets all user's goods from server
                    mainOperations.downloadUserData();
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
    }

    public void logOut() {
        Log.i(TAG, "logOut start");
        Call<ResponseBody> call = mDataManager.logOut();
        Response<ResponseBody> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                mDataManager.getPreferenceManager().logoutUser();
                DatabaseOperation.clearDb();
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
    }

    public static void getToken() {
//        Call<TokenRes> call = mDataManager.getToken();
//        call.enqueue(new Callback<TokenRes>() {
//            @Override
//            public void onResponse(@NonNull Call<TokenRes> call, @NonNull Response<TokenRes> response) {
//                if (response.code() == 200 && response.body() != null) {
//                    Toast.makeText(App.getContext(), response.body().getToken(), Toast.LENGTH_SHORT).show();
//                } else {
//                    System.out.println("Null response");
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<TokenRes> call, @NonNull Throwable t) {
//            }
//        });
    }

    private static boolean createLogo(@NonNull Logo logo, Filepath filepath) {
        Log.i(TAG, "createLogo");
        Call<UploadLogoRes> call = mDataManager.sendLogo(new UploadLogoReq(logo.getFilename(), filepath, logo.getLogo()));
        Response<UploadLogoRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    logo.setFid(response.body().getFid());
                    mDataManager.updateLogo(logo);
                    return true;
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }

    public Logo getLogo(String fid) {
        Call<GetFileRes> call = mDataManager.getLogo(fid);
        Logo logo = null;
        Response<GetFileRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    logo = new Logo(fid, response.body().getFilename(), response.body().getFile());
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return logo;
    }

    public List<Card> downloadMyCards() {
        Log.i(TAG, "downloadMyCards start");
        Call<GetUserRes> call = mDataManager.getUser();
        List<Card> cards = new ArrayList<>();
        Response<GetUserRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    List<String> existedCards = new ArrayList<>();
                    try {
                        List list = (List) ((Map) response.body().getFieldMyVcards())
                                .get(((Map) response.body().getFieldMyVcards())
                                        .keySet().iterator().next());
                        for (int i = 0; i < list.size(); i++) {
                            Map o = (Map) list.get(i);
                            Set set = o.keySet();
                            existedCards.add(o.get(set.iterator().next()).toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (String s : existedCards) {
                        cards.add(downloadCard(s, true));
                    }
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return cards;
    }

    public List<Card> downloadMyContacts() {
        Log.i(TAG, "downloadMyContacts start");
        Call<GetUserRes> call = mDataManager.getUser();
        List<Card> cards = new ArrayList<>();
        Response<GetUserRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    List<String> existedContacts = new ArrayList<>();
                    try {
                        List list = (List) ((Map) response.body().getFieldMyContacts())
                                .get(((Map) response.body().getFieldMyContacts())
                                        .keySet().iterator().next());
                        for (int i = 0; i < list.size(); i++) {
                            Map o = (Map) list.get(i);
                            Set set = o.keySet();
                            existedContacts.add(o.get(set.iterator().next()).toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (String s : existedContacts) {
                        cards.add(downloadCard(s, false));
                    }
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return cards;
    }

    public List<Group> getMyGroups() {
        Log.i(TAG, "getMyGroups start");
        Call<GetUserRes> call = mDataManager.getUser();
        List<Group> groups = new ArrayList<>();
        Response<GetUserRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    List<String> existedGroups = new ArrayList<>();
                    try {
                        List list = (List) ((Map) response.body().getFieldGroups())
                                .get(((Map) response.body().getFieldGroups())
                                        .keySet().iterator().next());
                        for (int i = 0; i < list.size(); i++) {
                            Map o = (Map) list.get(i);
                            Set set = o.keySet();
                            existedGroups.add(o.get(set.iterator().next()).toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (String s : existedGroups) {
                        groups.add(getGroup(s));
                    }
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return groups;
    }

    public List<String> getGroupContacts(String group) {
        Log.i(TAG, "getGroupContacts start for group: " + group);
        Call<GetGroupRes> call = mDataManager.getGroup(group);
        List<String> strings = new ArrayList<>();
        Response<GetGroupRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    List list;
                    try {
                        list = (List) ((Map) response.body().getFieldMyContacts())
                                .get(((Map) response.body().getFieldMyContacts()).keySet().iterator().next());
                        for (int i = 0; i < list.size(); i++) {
                            Map o = (Map) list.get(i);
                            Set set = o.keySet();
                            strings.add(o.get(set.iterator().next()).toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return strings;
    }

    public static boolean createCard(Card card) {
        Log.i(TAG, "createCard start");
        if (card.isMy()) {
            if (card.getLogo() != null) {
                createLogo(card.getLogo(), Filepath.VCARDS);
            }
            System.out.println(card.getLogo().getId());
            CreateCardReq createCardReq = new CreateCardReq(card);
            Call<CreateCardRes> call = mDataManager.sendCard(createCardReq);
            Response<CreateCardRes> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                Log.e(TAG, "Call.execute", e);
                return false;
            }
            if (response != null) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        card.setRemoteId(response.body().getId());
                        mDataManager.updateCard(card);
                        return addUserGoods(ListRole.card, new SimpleUnd(response.body().getId()));
                    } else {
                        Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                    }
                } else {
                    Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Null response");
            }
        }
        return false;
    }

    public static boolean updateCard(Card card) {
        Log.i(TAG, "updateCard start");
        if (card.isMy()) {
            if (card.getRemoteId() != null && !card.getRemoteId().isEmpty()) {
                if (card.getLogo() != null) {
                    createLogo(card.getLogo(), Filepath.VCARDS);
                }
                UpdateCardReq updateCardReq = new UpdateCardReq(card);
                Response<ResponseBody> response = null;
                Call<ResponseBody> call = mDataManager.updateCard(updateCardReq, card.getRemoteId());
                try {
                    response = call.execute();
                } catch (IOException e) {
                    Log.e(TAG, "Call.execute", e);
                    return false;
                }
                if (response != null) {
                    if (response.code() == 200) {
                        return true;
                    } else {
                        Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
                    }
                } else {
                    Log.e(TAG, "Null response");
                }
            }
        }
        return false;
    }

    public static boolean deleteCard(final String remoteId) {
        Log.i(TAG, "deleteCard start");
        Call<GetUserRes> call = mDataManager.getUser();
        Response<GetUserRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
            return false;
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    List<SimpleUnd> existedCards = new ArrayList<>();
                    try {
                        List list = (List) ((Map) response.body().getFieldMyVcards())
                                .get(((Map) response.body().getFieldMyVcards())
                                        .keySet().iterator().next());
                        for (int i = 0; i < list.size(); i++) {
                            Map o = (Map) list.get(i);
                            Set set = o.keySet();
                            String s = o.get(set.iterator().next()).toString();
                            existedCards.add(new SimpleUnd(s));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < existedCards.size(); i++) {
                        if (existedCards.get(i).getTargetId().equals(remoteId)) {
                            existedCards.remove(i);
                            break;
                        }
                    }
                    return updateUser(new UpdateCardsReq(existedCards));
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }

    public static boolean addContact(String remoteId) {
        Log.i(TAG, "addContact start");
        return addUserGoods(ListRole.contact, new SimpleUnd(remoteId));
    }

    public static boolean deleteContact(final String remoteId) {
        Log.i(TAG, "deleteContact start");
        Call<GetUserRes> call = mDataManager.getUser();
        Response<GetUserRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
            return false;
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    List<SimpleUnd> existedContacts = new ArrayList<>();
                    try {
                        List list = (List) ((Map) response.body().getFieldMyContacts())
                                .get(((Map) response.body().getFieldMyContacts())
                                        .keySet().iterator().next());
                        for (int i = 0; i < list.size(); i++) {
                            Map o = (Map) list.get(i);
                            Set set = o.keySet();
                            String s = o.get(set.iterator().next()).toString();
                            existedContacts.add(new SimpleUnd(s));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < existedContacts.size(); i++) {
                        if (existedContacts.get(i).getTargetId().equals(remoteId)) {
                            existedContacts.remove(i);
                            break;
                        }
                    }
                    return updateUser(new UpdateContactsReq(existedContacts));
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }

    public static boolean createGroup(Group group, List<String> contactsIds) {
        Log.i(TAG, "createGroup start");
        CreateGroupReq createGroupReq = new CreateGroupReq(group, contactsIds);
        Call<CreateGroupRes> call = mDataManager.sendGroup(createGroupReq);
        Response<CreateGroupRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
            return false;
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    group.setRemoteId(response.body().getId());
                    mDataManager.updateGroup(group);
                    return addUserGoods(ListRole.group, new SimpleUnd(response.body().getId()));
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }

    public static boolean updateGroup(Group group) {
        Log.i(TAG, "updateGroup start");
        if (group.getRemoteId() != null && !group.getRemoteId().isEmpty()) {
            if (group.getLogo() != null) {
                createLogo(group.getLogo(), Filepath.GROUPS);
            }
            UpdateGroupReq updateGroupReq = new UpdateGroupReq(group);
            Call<ResponseBody> call = mDataManager.updateGroup(updateGroupReq);
            Response<ResponseBody> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                Log.e(TAG, "Call.execute", e);
                return false;
            }
            if (response != null) {
                if (response.code() == 200) {
                    return true;
                } else {
                    Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Null response");
            }
        }
        return false;
    }

    public static boolean updateGroupContacts(String remoteId, List<String> contacts) {
        UpdateGroupReq updateGroupReq = new UpdateGroupReq(remoteId, contacts);
        Call<ResponseBody> call = mDataManager.updateGroup(updateGroupReq);
        Response<ResponseBody> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
            return false;
        }
        if (response != null) {
            if (response.code() == 200) {
                return true;
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }

    public static boolean deleteGroup(final String remoteId) {
        Log.i(TAG, "deleteGroup start");
        Call<GetUserRes> call = mDataManager.getUser();
        Response<GetUserRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
            return false;
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    List<SimpleUnd> existedGroups = new ArrayList<>();
                    try {
                        List list = (List) ((Map) response.body().getFieldGroups())
                                .get(((Map) response.body().getFieldGroups())
                                        .keySet().iterator().next());
                        for (int i = 0; i < list.size(); i++) {
                            Map o = (Map) list.get(i);
                            Set set = o.keySet();
                            String s = o.get(set.iterator().next()).toString();
                            existedGroups.add(new SimpleUnd(s));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < existedGroups.size(); i++) {
                        if (existedGroups.get(i).getTargetId().equals(remoteId)) {
                            existedGroups.remove(i);
                            break;
                        }
                    }
                    return updateUser(new UpdateGroupsReq(existedGroups));
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }

    private Card downloadCard(String cardId, boolean isMy) {
        Log.i(TAG, "downloadCard start for card: " + cardId);
        Call<GetCardRes> call = mDataManager.getCard(cardId);
        Card card = null;
        Response<GetCardRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    card = new Card();
                    card.setRemoteId(cardId);
                    card.setMy(isMy);
                    card.setTitle(response.body().getTitle());
                    card.setAddress(response.body().getAddress());
                    card.setCompany(response.body().getCompany());
                    try {
                        String s = null;
                        List list = (List) ((Map) response.body().getFieldLogotype())
                                .get(((Map) response.body().getFieldLogotype())
                                        .keySet().iterator().next());
                        for (int i = 0; i < list.size(); i++) {
                            Map o = (Map) list.get(i);
                            Set set = o.keySet();
                            s = o.get(set.iterator().next()).toString();
                        }
                        Logo logo = getLogo(s);
                        mDataManager.addLogo(logo);
                        card.setLogo(logo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    card.setMidlename(response.body().getMiddleName());
                    card.setName(response.body().getName());
                    card.setPosition(response.body().getPosition());
                    card.setSurname(response.body().getSurname());
                    card.setSite(response.body().getWebSite());
                    List<SocialLink> links = null;
                    try {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        Object o = gson.fromJson(response.body().getSocialLinks(), Object.class);
                        Map<String, String> map = (Map) o;
                        Set<String> set = map.keySet();
                        links = new ArrayList<>();
                        for (String type : set) {
                            if (map.get(type) != null && !map.get(type).isEmpty()) {
                                SocialLink link = new SocialLink(LinkType.getType(type), map.get(type));
                                links.add(link);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    card.setSocialLinks(links);
                    card.setBase(new Base(response.body().getBase64Vcard()));
                    card.addPhone(new Phone(response.body().getPhone()));//TODO: Upgrade to multiple fields
                    card.addEmail(new Email(response.body().getEmail()));//TODO: Upgrade to multiple fields
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return card;
    }

    private Group getGroup(String id) {
        Log.i(TAG, "getGroup start");
        Call<GetGroupRes> call = mDataManager.getGroup(id);
        Group group = null;
        Response<GetGroupRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    group = new Group();
                    try {
                        group.setRemoteId(response.body().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    group.setName(response.body().getGroupName());
                    group.setDescription(response.body().getDescription());
                    try {
                        List list = (List) ((Map) response.body().getFieldLogotype())
                                .get(((Map) response.body().getFieldLogotype()).keySet().iterator().next());
                        Map o = (Map) list.get(0);
                        Set set = o.keySet();
                        Logo logo = getLogo(o.get(set.iterator().next()).toString());
                        mDataManager.addLogo(logo);
                        group.setLogo(logo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return group;
    }

    private static boolean addUserGoods(final ListRole role, final SimpleUnd und) {
        Log.i(TAG, "addUserGoods");
        Call<GetUserRes> call = mDataManager.getUser();
        Response<GetUserRes> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
            return false;
        }
        if (response != null) {
            if (response.code() == 200) {
                if (response.body() != null) {
                    switch (role) {
                        case card:
                            Log.i(TAG, "addUserGoods role: card");
                            List<SimpleUnd> existedCards = new ArrayList<>();
                            try {
                                List list = (List) ((Map) response.body().getFieldMyVcards())
                                        .get(((Map) response.body().getFieldMyVcards())
                                                .keySet().iterator().next());
                                for (int i = 0; i < list.size(); i++) {
                                    Map o = (Map) list.get(i);
                                    Set set = o.keySet();
                                    String s = o.get(set.iterator().next()).toString();
                                    existedCards.add(new SimpleUnd(s));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            existedCards.add(und);
                            for (SimpleUnd u : existedCards) {
                                u.setTargetId(u.getTargetId());
                            }
                            updateUser(new UpdateCardsReq(existedCards));
                            return true;
                        case contact:
                            List<SimpleUnd> existedContacts = new ArrayList<>();
                            try {
                                List list = (List) ((Map) response.body().getFieldMyContacts())
                                        .get(((Map) response.body().getFieldMyContacts())
                                                .keySet().iterator().next());
                                for (int i = 0; i < list.size(); i++) {
                                    Map o = (Map) list.get(i);
                                    Set set = o.keySet();
                                    String s = o.get(set.iterator().next()).toString();
                                    existedContacts.add(new SimpleUnd(s));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            existedContacts.add(und);
                            for (SimpleUnd u : existedContacts) {
                                u.setTargetId(u.getTargetId());
                            }
                            updateUser(new UpdateContactsReq(existedContacts));
                            return true;
                        case group:
                            List<SimpleUnd> existedGroups = new ArrayList<>();
                            try {
                                List list = (List) ((Map) response.body().getFieldGroups())
                                        .get(((Map) response.body().getFieldGroups())
                                                .keySet().iterator().next());
                                for (int i = 0; i < list.size(); i++) {
                                    Map o = (Map) list.get(i);
                                    Set set = o.keySet();
                                    String s = o.get(set.iterator().next()).toString();
                                    existedGroups.add(new SimpleUnd(s));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            existedGroups.add(und);
                            for (SimpleUnd u : existedGroups) {
                                u.setTargetId(u.getTargetId());
                            }
                            updateUser(new UpdateGroupsReq(existedGroups));
                            return true;
                        default:
                            return false;
                    }
                } else {
                    Log.e(TAG, "Null response body" + response.code() + "  " + response.message());
                }
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }

    private static boolean updateUser(UpdateCardsReq collection) {
        Call<ResponseBody> call = mDataManager.updateUser(collection);
        Response<ResponseBody> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
            return false;
        }
        if (response != null) {
            if (response.code() == 200) {
                return true;
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }

    private static boolean updateUser(UpdateContactsReq collection) {
        Call<ResponseBody> call = mDataManager.updateUser(collection);
        Response<ResponseBody> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
            return false;
        }
        if (response != null) {
            if (response.code() == 200) {
                return true;
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }

    private static boolean updateUser(UpdateGroupsReq collection) {
        Call<ResponseBody> call = mDataManager.updateUser(collection);
        Response<ResponseBody> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, "Call.execute", e);
            return false;
        }
        if (response != null) {
            if (response.code() == 200) {
                return true;
            } else {
                Log.e(TAG, "Unchecked response code" + response.code() + "  " + response.message());
            }
        } else {
            Log.e(TAG, "Null response");
        }
        return false;
    }
}
package coms309.Websockets;

import coms309.Athletes.Athlete;
import coms309.Athletes.AthleteRepository;
import coms309.Coaches.Coach;
import coms309.Coaches.CoachRepository;
import coms309.Managers.Manager;
import coms309.Users.User;
import coms309.Users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {


    private static UserRepository userRepository;

    private static AthleteRepository athleteRepository;

    private static CoachRepository coachRepository;
    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;
    }
    @Autowired
    public void setAthleteRepository(AthleteRepository repo) {
        athleteRepository = repo;
    }
    @Autowired
    public void setCoachRepository(CoachRepository repo) {
        coachRepository = repo;
    }
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();
    private static Map < String, String > usernamePrefix = new Hashtable<>();
    private static Map<String, User> usernameUserMap = new Hashtable<>();
    private static Map<User, String> userUsernameMap = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") int userId)
            throws IOException {
        logger.info("Entered into Open");
        User user = userRepository.findById(userId);
        if(user == null) {
            return;
        }
        String username = user.getEmailAddress().split("@")[0];

        String prefix = "";
        switch(user.getClassType())
        {
            case 1:
                prefix = "[Athlete] ";
                break;
            case 2:
                prefix = "[Coach] ";
                break;
            case 3:
                prefix = "[Manager] ";
                break;
            default:
                break;
        }
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
        usernamePrefix.put(username, prefix);
        usernameUserMap.put(username, user);
        userUsernameMap.put(user, username);
        String message = "User:" + " " + prefix + username + " has Joined the Chat";
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages
        logger.info("Entered into Message: Got Message:" + message);
        String username = sessionUsernameMap.get(session);
        String prefix = usernamePrefix.get(username);
        User user = null;
        if (message.startsWith("@")) // Direct message to a user using the format "@username <message>"
        {
            String destUsername = message.split(" ")[0].substring(1); // don't do this in your code!
            if(destUsername.equals("Athletes"))
            {
                user = usernameUserMap.get(username);
                if(user == null) return;
                Coach coach = user.getCoach();
                if(coach == null) return;
                List<Athlete> athleteList = athleteRepository.findAll();
                int athleteUserId = 0;
                for(Athlete athlete: athleteList) {
                    athleteUserId = athlete.getUser().getId();
                    if(athlete.getCoach() != null && athlete.getCoach().getId() == coach.getId())
                    {
                        user = userRepository.findById(athleteUserId);
                        destUsername = user.getEmailAddress().split("@")[0];
                        if(usernamePrefix.containsKey(destUsername)){
                            if(message.split(" ").length > 1) message = message.split(" ")[1];
                            sendMessageToPArticularUser(destUsername, "[Coach Broadcast] " +
                                    prefix + username + ":" + message);
                        }
                    }
                }
                sendMessageToPArticularUser(username, "[Coach Broadcast] " + prefix + username +
                        ":" + message);
            }
            else if(destUsername.equals("Coaches")) {
                user = usernameUserMap.get(username);
                if(user == null) return;
                Manager manager = user.getManager();
                if(manager == null) return;
                List<Coach> coachList = coachRepository.findAll();
                int coachUserId = 0;
                for(Coach coach: coachList) {
                    coachUserId = coach.getUser().getId();
                    if(coach.getManager() != null && coach.getManager().getId() == manager.getId())
                    {
                        user = userRepository.findById(coachUserId);
                        destUsername = user.getEmailAddress().split("@")[0];
                        if(usernamePrefix.containsKey(destUsername)){
                            if(message.split(" ").length > 1) message = message.split(" ")[1];
                            sendMessageToPArticularUser(destUsername, "[Manager Broadcast] " +
                                    prefix + username + ":" + message);
                        }
                    }
                }
                sendMessageToPArticularUser(username, "[Manager Broadcast] " + prefix + username +
                        ":" + message);
            }
            else {
                sendMessageToPArticularUser(destUsername, "[DM] " + prefix + username + ": " + message);
                sendMessageToPArticularUser(username, "[DM] " + prefix + username + ": " + message);
            }
        } else // Message to whole chat
        {
            broadcast(prefix + username + ": " + message);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        String username = sessionUsernameMap.get(session);
        String prefix = usernamePrefix.get(username);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        usernamePrefix.remove(username);
        usernameUserMap.remove(username);
        String message = prefix + username + " disconnected";
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
    }

    private void sendMessageToPArticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }

        });

    }
}

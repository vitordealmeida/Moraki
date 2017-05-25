package br.com.serissa.moraki;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

class Message {
    private static int lastMessageId;

    private static final IUser bot = new IUser() {
        @Override
        public String getId() {
            return "0";
        }

        @Override
        public String getName() {
            return "Moraki bot";
        }

        @Override
        public String getAvatar() {
            return null;
        }
    };

    private static final IUser usuario = new IUser() {
        @Override
        public String getId() {
            return "1";
        }

        @Override
        public String getName() {
            return "Usuário";
        }

        @Override
        public String getAvatar() {
            return null;
        }
    };

    private static IMessage createMessage(final String message, final boolean isUser) {
        return new IMessage() {
            Date creationDate = new Date(System.currentTimeMillis());

            @Override
            public String getId() {
                return String.valueOf(lastMessageId++);
            }

            @Override
            public String getText() {
                return message;
            }

            @Override
            public IUser getUser() {
                return isUser ? usuario : bot;
            }

            @Override
            public Date getCreatedAt() {
                return creationDate;
            }
        };
    }

    static IMessage fromUser(final String message) {
        return createMessage(message, true);
    }

    static IMessage fromBot(final String message) {
        return createMessage(message, false);
    }

    static String getUserId() {
        return usuario.getId();
    }
}

import { RtcTokenBuilder, RtcRole } from "agora-access-token";
import { APP_ID, APP_CERT, EXPIRATION_TIME } from "../config.json";

export default function(channelName: string): TokenInfo {
    // Rtc Examples
    const uid: number = Math.floor(Math.random() * 1000000000);
    const role = RtcRole.PUBLISHER;

    const privilegeExpiredTs = Math.floor(Date.now() / 1000) + EXPIRATION_TIME;

    // Build token with uid
    const token = RtcTokenBuilder.buildTokenWithUid(APP_ID, APP_CERT, channelName, uid, role, privilegeExpiredTs);

    return {
        token,
        uid
    };
}

interface TokenInfo {
    token: string;
    uid: number;
}

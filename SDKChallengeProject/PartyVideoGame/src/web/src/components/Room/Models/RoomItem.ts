import { VideoStream } from "utils/VideoClient";
import { RoomItemType } from "./RoomItemType";

export class RoomItem {
    id!: string;
    content: any;
    order!: number;
    roomItemType!:RoomItemType;
    localVideoStream?:VideoStream;
}
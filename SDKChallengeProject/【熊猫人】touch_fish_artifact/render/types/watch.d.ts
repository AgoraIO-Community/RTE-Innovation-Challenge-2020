interface VideoContainerInfo {
    id: string;
    uid: number | null;
    type: 'blank' | 'stream' | 'fish' | 'self';
    videoSrc?: string;
    timeOut?: number;
}
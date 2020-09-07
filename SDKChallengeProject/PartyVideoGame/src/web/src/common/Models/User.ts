export class User {
    id: string = '';
    name?: string = '';
    photoUrl: string | null = null;
    provider: string | null = null;
    accessToken: string = '';
    expireDate: Date | null = null;
    culture: string | null = null;
    refreshToken: string = '';
}

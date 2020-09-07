export class Line {
    constructor(x0: number, y0: number, x1: number, y1: number) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    x0!: number;
    y0!: number;
    x1!: number;
    y1!: number;

    distance() {
        return Math.sqrt(Math.pow((this.x0 - this.x1), 2) + Math.pow((this.y0 - this.y1), 2));
    }
}
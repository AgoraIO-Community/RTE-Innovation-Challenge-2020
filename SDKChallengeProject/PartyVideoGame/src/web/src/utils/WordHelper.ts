import sentencer from 'sentencer';

export class WordHelper {
    static newNoun(): string {
        return sentencer.make("{{ noun }}");
    }

    static newNounArray(count:number): Array<string> {
        var result=new Array<string>();
        for (let index = 0; index < count; index++) {
            result.push(this.newNoun())
        }
        return result;
    }

    static newAdjectiveNoun(): string {
        return sentencer.make("{{ adjective }}-{{ noun }}");
    }
}
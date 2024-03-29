export interface ILabel {
  id?: number;
  label?: string | null;
  desc?: string | null;
  fakeNumber?: number | null;
  someFaker?: string | null;
}

export class Label implements ILabel {
  constructor(
    public id?: number,
    public label?: string | null,
    public desc?: string | null,
    public fakeNumber?: number | null,
    public someFaker?: string | null,
  ) {}
}

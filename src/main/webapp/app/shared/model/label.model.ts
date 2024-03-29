import { type ITicket } from '@/shared/model/ticket.model';

export interface ILabel {
  id?: number;
  label?: string | null;
  desc?: string | null;
  fakeNumber?: number | null;
  tickets?: ITicket[] | null;
}

export class Label implements ILabel {
  constructor(
    public id?: number,
    public label?: string | null,
    public desc?: string | null,
    public fakeNumber?: number | null,
    public tickets?: ITicket[] | null,
  ) {}
}

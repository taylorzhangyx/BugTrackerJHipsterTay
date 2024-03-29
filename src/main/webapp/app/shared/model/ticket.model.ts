import { type IProject } from '@/shared/model/project.model';
import { type IUser } from '@/shared/model/user.model';
import { type ILabel } from '@/shared/model/label.model';

export interface ITicket {
  id?: number;
  title?: string;
  description?: string | null;
  dueDate?: Date | null;
  done?: boolean | null;
  project?: IProject | null;
  assignedTo?: IUser | null;
  labels?: ILabel[] | null;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string | null,
    public dueDate?: Date | null,
    public done?: boolean | null,
    public project?: IProject | null,
    public assignedTo?: IUser | null,
    public labels?: ILabel[] | null,
  ) {
    this.done = this.done ?? false;
  }
}

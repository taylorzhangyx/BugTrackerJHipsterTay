import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Project = () => import('@/entities/project/project.vue');
const ProjectUpdate = () => import('@/entities/project/project-update.vue');
const ProjectDetails = () => import('@/entities/project/project-details.vue');

const Label = () => import('@/entities/label/label.vue');
const LabelUpdate = () => import('@/entities/label/label-update.vue');
const LabelDetails = () => import('@/entities/label/label-details.vue');

const Ticket = () => import('@/entities/ticket/ticket.vue');
const TicketUpdate = () => import('@/entities/ticket/ticket-update.vue');
const TicketDetails = () => import('@/entities/ticket/ticket-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'project',
      name: 'Project',
      component: Project,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project/new',
      name: 'ProjectCreate',
      component: ProjectUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project/:projectId/edit',
      name: 'ProjectEdit',
      component: ProjectUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project/:projectId/view',
      name: 'ProjectView',
      component: ProjectDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'label',
      name: 'Label',
      component: Label,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'label/new',
      name: 'LabelCreate',
      component: LabelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'label/:labelId/edit',
      name: 'LabelEdit',
      component: LabelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'label/:labelId/view',
      name: 'LabelView',
      component: LabelDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ticket',
      name: 'Ticket',
      component: Ticket,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ticket/new',
      name: 'TicketCreate',
      component: TicketUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ticket/:ticketId/edit',
      name: 'TicketEdit',
      component: TicketUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ticket/:ticketId/view',
      name: 'TicketView',
      component: TicketDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};

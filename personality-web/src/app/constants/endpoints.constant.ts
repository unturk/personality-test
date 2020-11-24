import {environment} from '../../environments/environment';

export const endpoints = {
  answers: () => `${environment.apiUrlPrefix}/answers/`,
  categories: () => `${environment.apiUrlPrefix}/categories/`,
  questions: (id: number) => `${environment.apiUrlPrefix}/questions?category=${id}`,
};

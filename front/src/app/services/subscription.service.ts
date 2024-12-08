import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ThemeDTO} from "../dtos/theme-dto";
import { SubscriptionDTO } from "../dtos/subscription-dto";

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  private apiUrl = '/api/subscriptions';

  constructor(private http: HttpClient) {
  }

  // Récupérer les abonnements de l'utilisateur
  getUserSubscriptions(token: string): Observable<ThemeDTO[]> {
    const headers = {Authorization: `Bearer ${token}`};
    return this.http.get<ThemeDTO[]>(`${this.apiUrl}/user/subscriptions`, {headers});
  }

  // S'abonner à un thème
  subscribe(themeId: number): Observable<SubscriptionDTO> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.post<SubscriptionDTO>(`${this.apiUrl}/subscribe/${themeId}`, {}, { headers });
  }

  // Se désabonner d'un thème
  unsubscribe(themeId: number): Observable<SubscriptionDTO> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.post<SubscriptionDTO>(`${this.apiUrl}/unsubscribe/${themeId}`, {}, { headers });
  }
}
